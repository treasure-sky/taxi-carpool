package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyUpdateRequestDTO;
import org.springframework.stereotype.Component;


@Component
public class PartyMapper {

    public PartyResponseDTO convertToResponseDTO(PartyEntity partyEntity) {
        return new PartyResponseDTO(
            partyEntity.getId(),
            partyEntity.getName(),
            partyEntity.isDeleted(),
            partyEntity.getMemberEntities(),
            partyEntity.getHostMemberId(),
            partyEntity.getEndDate()
        );
    }

    public PartyEntity convertToEntity(PartyCreateRequestDTO createRequestDTO) {
        return new PartyEntity(
            createRequestDTO.getId(),
            createRequestDTO.getName(),
            createRequestDTO.getMemberEntities(),
            createRequestDTO.getHostMemberId(),
            createRequestDTO.getEndDate()
        );
    }

    public PartyEntity convertToEntityByUpdate(PartyEntity partyEntity, PartyUpdateRequestDTO partyUpdateRequestDTO) {
        return partyEntity.updateParty(
            partyUpdateRequestDTO.getName(),
            partyUpdateRequestDTO.isDeleted(),
            partyUpdateRequestDTO.getMemberEntities(),
            partyUpdateRequestDTO.getHostMemberId(),
            partyUpdateRequestDTO.getEndDate()
        );
    }
}
