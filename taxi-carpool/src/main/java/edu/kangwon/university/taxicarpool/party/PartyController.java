package edu.kangwon.university.taxicarpool.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<PartyDTO> getParty(
        @PathVariable("partyId") Long partyId
    ) {
        return partyService.getParty(partyId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PartyDTO> getPartyList() {
        return partyService.getPartyList();
    }

    @PostMapping
    public PartyDTO createParty() {
        return partyService.createParty();
    }

    @PutMapping("/{partyId}")
    public PartyDTO updateParty() {
        return partyService.updateParty();
    }

    @DeleteMapping("/{partyId}")
    public PartyDTO deleteParty() {
        return partyService.deleteParty();
    }


}
