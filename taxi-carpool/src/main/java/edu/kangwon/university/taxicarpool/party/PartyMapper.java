package edu.kangwon.university.taxicarpool.party;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<PartyDTO> convertToDTOList(List<PartyEntity> partyEntities) {
        return partyEntities.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


}
