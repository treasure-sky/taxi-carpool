package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyUpdateRequestDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<PartyResponseDTO> getParty(Long partyId) {
        Optional<PartyEntity> partyEntity = partyRepository.findById(partyId);
        return partyEntity.map(partyMapper::convertToResponseDTO);
    }

    public Page<PartyResponseDTO> getPartyList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PartyEntity> partyEntities = partyRepository.findAll(pageable);

        return partyEntities.map(partyMapper::convertToResponseDTO);
    }

    @Transactional
    public PartyResponseDTO createParty(PartyCreateRequestDTO createRequestDTO) {
        if (createRequestDTO.getName() == null || createRequestDTO.getName().isEmpty()) {
            // 예외처리 일단 대충만 해놓음.
            throw new IllegalArgumentException("파티 이름은 필수 입력 항목입니다.");
        }

        PartyEntity partyEntity = partyMapper.convertToEntity(createRequestDTO);

        // 프론트로부터 파티를 만드는 멤버의 Id를 createRequestDTO 내부 필드에 넣어서 보내달라고 해야함.
        Long memberId = createRequestDTO.getMemberId();
        if (memberId != null) {
            partyEntity.updateParty(
                partyEntity.getName(),
                partyEntity.isDeleted(),
                partyEntity.getMemberEntities(),
                memberId,  // hostMemberId = memberId임(방을 만든 멤버가 최초 호스트)
                partyEntity.getEndDate()
            );
            // 처음 방 만든 멤버도 그 파티방의 멤버로 등록하는 것임.(이거 안 해놓으면 프론트한테 요청 2번 요청해야함.)
            MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 멤버가 존재하지 않습니다."));
            partyEntity.getMemberEntities().add(member);
        }
        else {
            throw new RuntimeException("memberId가 null임.");
        }

        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }

    @Transactional
    public PartyResponseDTO updateParty(Long partyId, Long memberId, PartyUpdateRequestDTO updateRequestDTO) {
        //  예외처리 일단 대충만 해놓음
        PartyEntity existingPartyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));

        if (existingPartyEntity.getHostMemberId() != memberId) {
            throw new RuntimeException("호스트만 수정할 수 있습니다.");
        }

        partyMapper.convertToEntityByUpdate(existingPartyEntity, updateRequestDTO);

        PartyEntity savedPartyEntity = partyRepository.save(existingPartyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }


    @Transactional
    public Map<String, Object> deleteParty(Long partyId, Long memberId) {
        // 예외처리 일단 대충만 해놓음
        PartyEntity partyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));
        if(partyEntity.getHostMemberId() != memberId) {
            throw new RuntimeException("호스트만 삭제 할 수 있습니다.");
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
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NoSuchElementException("해당 멤버가 존재하지 않습니다."));

        if (party.isDeleted()) {
            throw new IllegalStateException("이미 삭제된 파티입니다.");
        }

        party.getMemberEntities().add(member);

        PartyEntity savedParty = partyRepository.save(party);
        return partyMapper.convertToResponseDTO(savedParty);
    }

    @Transactional
    public PartyResponseDTO leaveParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NoSuchElementException("해당 멤버가 존재하지 않습니다."));

        if (!party.getMemberEntities().contains(member)) {
            throw new IllegalArgumentException("이 멤버는 파티에 속해있지 않습니다.");
        }

        boolean isHostLeaving = (party.getHostMemberId() != null
            && party.getHostMemberId().equals(memberId));

        // 파티에서 멤버 제거
        party.getMemberEntities().remove(member);

        // 호스트인 멤버가 파티를 떠나려고 할 때의 로직.
        if (isHostLeaving) {
            List<MemberEntity> remaining = party.getMemberEntities();
            if (remaining.isEmpty()) {
                // 아무도 없으면 삭제 처리
                party.setDeleted(true);
                throw new IllegalArgumentException("파티방의 모든 멤버가 떠나여, 파티방이 삭제되었습니다.");
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
