package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyUpdateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
        PartyResponseDTO partyResponse = partyService.getParty(partyId);
        return ResponseEntity.ok(partyResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PartyResponseDTO>> getPartyList(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") Integer page,
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.") Integer size
    ) {
        Page<PartyResponseDTO> partyList = partyService.getPartyList(page, size);
        return ResponseEntity.ok(partyList);
    }

    @GetMapping("/custom")
    public ResponseEntity<Page<PartyResponseDTO>> getCustomPartyList(
        @RequestParam(required = false) Double userDepartureLng,
        @RequestParam(required = false) Double userDepartureLat,
        @RequestParam(required = false) Double userDestinationLng,
        @RequestParam(required = false) Double userDestinationLat,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime userDepartureTime,
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") Integer page,
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.") Integer size
    ) {
        Page<PartyResponseDTO> partyList = partyService.getCustomPartyList(userDepartureLng,
            userDepartureLat,
            userDestinationLng, userDestinationLat,
            userDepartureTime, page, size);
        return ResponseEntity.ok(partyList);
    }

    @PostMapping
    public ResponseEntity<PartyResponseDTO> createParty(
        @RequestBody @Valid PartyCreateRequestDTO createRequestDTO
    ) {
        return ResponseEntity.ok(partyService.createParty(createRequestDTO));
    }

    @PutMapping("/{partyId}")
    public ResponseEntity<PartyResponseDTO> updateParty(
        @RequestBody @Valid PartyUpdateRequestDTO updateRequestDTO,
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
