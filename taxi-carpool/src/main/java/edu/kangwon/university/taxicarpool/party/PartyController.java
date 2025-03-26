package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyUpdateRequestDTO;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<PartyResponseDTO> getParty(
        @PathVariable("partyId") Long partyId
    ) {
        return partyService.getParty(partyId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<PartyResponseDTO>> getPartyList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(partyService.getPartyList(page, size));
    }

    @PostMapping
    public ResponseEntity<PartyResponseDTO> createParty(
        @RequestBody PartyCreateRequestDTO createRequestDTO
    ) {
        return ResponseEntity.ok(partyService.createParty(createRequestDTO));
    }

    @PutMapping("/{partyId}")
    public ResponseEntity<PartyResponseDTO> updateParty(
        @RequestBody PartyUpdateRequestDTO updateRequestDTO,
        @RequestParam Long memberId,
        @PathVariable Long partyId
    ) {
        return ResponseEntity.ok(partyService.updateParty(partyId, memberId, updateRequestDTO));
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<Map<String, Object>> deleteParty(
        @PathVariable Long partyId,
        @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(partyService.deleteParty(partyId, memberId));
    }

    // 파티방에 멤버 추가하는 엔트포인트
    @PostMapping("/{partyId}/join")
    public ResponseEntity<PartyResponseDTO> joinParty(
        @PathVariable Long partyId,
        @RequestParam Long memberId
    ) {
        PartyResponseDTO result = partyService.joinParty(partyId, memberId);
        return ResponseEntity.ok(result);
    }

    // 파티방에서 멤버가 퇴장할 때 엔드포인트
    @PostMapping("/{partyId}/leave")
    public ResponseEntity<PartyResponseDTO> leaveParty(
        @PathVariable Long partyId,
        @RequestParam Long memberId
    ) {
        PartyResponseDTO result = partyService.leaveParty(partyId, memberId);
        return ResponseEntity.ok(result);
    }


}
