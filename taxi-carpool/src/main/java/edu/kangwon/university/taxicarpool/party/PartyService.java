package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyUpdateRequestDTO;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyAlreadyDeletedException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyEmptyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyFullException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.UnauthorizedHostAccessException;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;
    // 추후에 merge되면 memberService로 바꿔서 해도 괜찮을듯
    private final MemberRepository memberRepository;

    @Autowired
    PartyService(PartyRepository partyRepository,
        PartyMapper partyMapper,
        MemberRepository memberRepository
    ) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.memberRepository = memberRepository;
    }

    public PartyResponseDTO getParty(Long partyId) {
        PartyEntity partyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        return partyMapper.convertToResponseDTO(partyEntity);
    }

    public Page<PartyResponseDTO> getPartyList(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("페이지 번호 또는 페이지 크기가 올바르지 않습니다.");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PartyEntity> partyEntities = partyRepository.findAll(pageable);
        return partyEntities.map(partyMapper::convertToResponseDTO);
    }

    @Transactional
    public PartyResponseDTO createParty(PartyCreateRequestDTO createRequestDTO) {

        PartyEntity partyEntity = partyMapper.convertToEntity(createRequestDTO);

        // 프론트로부터 파티를 만드는 멤버의 Id(creatorMemberId)를 createRequestDTO 내부 필드(creatorMemberId)에 넣어서 보내달라고 해야함.
        Long creatorMemberId = createRequestDTO.getCreatorMemberId();

        if (creatorMemberId != null) {
            partyEntity.setHostMemberId(
                creatorMemberId); // creatorMemberId(파티를 만든 멤버의 ID)를 HostMemberId로 설정
        } else {
            throw new IllegalArgumentException("파티방을 만든 멤버의 Id가 null임.");
        }

        // 처음 방 만든 멤버도 그 파티방의 멤버로 등록하는 것임.(이거 안 해놓으면 프론트한테 요청 2번 요청해야함.)
        MemberEntity member = memberRepository.findById(creatorMemberId)
            .orElseThrow(() -> new MemberNotFoundException("파티방을 만든 멤버가 존재하지 않습니다."));
        partyEntity.getMemberEntities().add(member);

        partyEntity.setCurrentParticipantCount(1); // 방 만들고, 현재 인원 1명으로 설정

        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }

    @Transactional
    public PartyResponseDTO updateParty(Long partyId, Long memberId,
        PartyUpdateRequestDTO updateRequestDTO) {
        PartyEntity existingPartyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));

        if (!existingPartyEntity.getHostMemberId().equals(memberId)) {
            throw new UnauthorizedHostAccessException("호스트만 수정할 수 있습니다.");
        }

        partyMapper.convertToEntityByUpdate(existingPartyEntity, updateRequestDTO);

        PartyEntity savedPartyEntity = partyRepository.save(existingPartyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }


    @Transactional
    public Map<String, Object> deleteParty(Long partyId, Long memberId) {
        PartyEntity partyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        if (!partyEntity.getHostMemberId().equals(memberId)) {
            throw new UnauthorizedHostAccessException("호스트만 삭제할 수 있습니다.");
        }

        partyEntity.setDeleted(true);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "파티가 성공적으로 삭제되었습니다.");
        response.put("deletedPartyId", partyId);

        return response;
    }

    // 멤버가 파티방의 멤버로 참가하는 로직의 메서드
    @Transactional
    public PartyResponseDTO joinParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        if (party.isDeleted()) {
            throw new PartyAlreadyDeletedException("이미 삭제된 파티입니다.");
        }
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다."));

        party.getMemberEntities().add(member);

        // 새로운 멤버가 파티 참가시, 현재인원 1명 추가
        int currentParticipantCount = party.getCurrentParticipantCount();
        if (currentParticipantCount < party.getMaxParticipantCount()) {
            party.setCurrentParticipantCount(currentParticipantCount++);
        } else {
            throw new PartyFullException("현재 파티의 참여중인 인원수가 가득찼습니다.");
        }

        PartyEntity savedParty = partyRepository.save(party);
        return partyMapper.convertToResponseDTO(savedParty);
    }

    @Transactional
    public PartyResponseDTO leaveParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다."));

        if (!party.getMemberEntities().contains(member)) {
            throw new MemberNotInPartyException("이 멤버는 해당 파티에 속해있지 않습니다.");
        }

        // 호스트인 멤버가 파티를 떠나려고 할 때의 로직을 위한 isHostLeaving
        boolean isHostLeaving = (party.getHostMemberId() != null
            && party.getHostMemberId().equals(memberId));

        // 파티에서 멤버 제거
        party.getMemberEntities().remove(member);

        // 파티의 현재 인원 수 감소시키기
        int currentParticipantCount = party.getCurrentParticipantCount();
        if (currentParticipantCount > 1) {
            party.setCurrentParticipantCount(currentParticipantCount--);
        } else {
            // 앱의 플로우상 마지막으로 떠나는 멤버가 호스트일수밖에 없어서, 해당 코드가 필요하지 않을 것 같긴한데, 혹시 몰라서 일단 추가해둠.
            party.setDeleted(true);
            throw new PartyEmptyException("파티방의 모든 멤버가 떠나여, 파티방이 삭제되었습니다.");
        }

        // 호스트인 멤버가 파티를 떠나려고 할 때의 로직.
        if (isHostLeaving) {
            List<MemberEntity> remaining = party.getMemberEntities();
            if (remaining.isEmpty()) {
                // 아무도 없으면 삭제 처리
                party.setDeleted(true);
                throw new PartyEmptyException("파티방의 모든 멤버가 떠나여, 파티방이 삭제되었습니다.");
            } else {
                // 첫 멤버를 새 호스트로(호스트 제외하고 가장 빨리 들어온 멤버)
                MemberEntity nextHost = remaining.get(0);
                party.setHostMemberId(nextHost.getId());
            }
        }

        PartyEntity saved = partyRepository.save(party);
        return partyMapper.convertToResponseDTO(saved);
    }


}
