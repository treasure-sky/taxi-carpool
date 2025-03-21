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
        @PathVariable Long partyId
    ) {
        return ResponseEntity.ok(partyService.updateParty(partyId, updateRequestDTO));
    }

    // ResponseEntity<Map<String, Object>>로 수정
    @DeleteMapping("/{partyId}")
    public ResponseEntity<Map<String, Object>> deleteParty(
        @PathVariable Long partyId
    ) {
        return ResponseEntity.ok(partyService.deleteParty(partyId));
    }


}
