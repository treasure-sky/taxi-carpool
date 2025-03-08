package edu.kangwon.university.taxicarpool.party;

import org.springframework.stereotype.Component;

@Component
public class PartyMapper {

    public PartyDTO convertToDTO(PartyEntity partyEntity) {
        return new PartyDTO(
            partyEntity.getId(),
            partyEntity.getName(),
            partyEntity.isDeleted(),
            partyEntity.getMemberEntities(),
            partyEntity.getStartDate(),
            partyEntity.getEndDate()
        );
    }


}
