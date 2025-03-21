package edu.kangwon.university.taxicarpool.party;

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

    @Autowired
    PartyService(PartyRepository partyRepository,
        PartyMapper partyMapper) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
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
        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }

    @Transactional
    public PartyResponseDTO updateParty(Long partyId, PartyUpdateRequestDTO updateRequestDTO) {
        //  예외처리 일단 대충만 해놓음
        PartyEntity existingPartyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));

        partyMapper.convertToEntityByUpdate(existingPartyEntity, updateRequestDTO);

        PartyEntity savedPartyEntity = partyRepository.save(existingPartyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }


    @Transactional
    public Map<String, Object> deleteParty(Long partyId) {
        // 예외처리 일단 대충만 해놓음
        PartyEntity partyEntity = partyRepository.findById(partyId)
            .orElseThrow(() -> new NoSuchElementException("해당 파티가 존재하지 않습니다."));

        partyRepository.delete(partyEntity);

        // 메시지 + 삭제된 ID 포함한 응답 반환
        // String이랑 Long타입 id까지 쓸거라 Object로 업캐스팅 해놓음
        Map<String, Object> response = new HashMap<>();
        response.put("message", "파티가 성공적으로 삭제되었습니다.");
        response.put("deletedPartyId", partyId);

        return response;
    }


}
