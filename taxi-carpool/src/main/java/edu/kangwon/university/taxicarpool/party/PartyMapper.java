package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.map.MapPlace;
import edu.kangwon.university.taxicarpool.map.MapPlaceDTO;
import edu.kangwon.university.taxicarpool.party.dto.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.dto.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.dto.PartyUpdateRequestDTO;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
public class PartyMapper {

    public PartyResponseDTO convertToResponseDTO(PartyEntity partyEntity) {
        MapPlace startPlace = partyEntity.getStartPlace();
        MapPlace endPlace = partyEntity.getEndPlace();
        MapPlaceDTO startDto = new MapPlaceDTO(
            startPlace.getName(),
            startPlace.getRoadAddressName(),
            startPlace.getX(),
            startPlace.getY()
        );
        MapPlaceDTO endDto = new MapPlaceDTO(
            endPlace.getName(),
            endPlace.getRoadAddressName(),
            endPlace.getX(),
            endPlace.getY()
        );

        List<Long> memberIds = partyEntity.getMemberEntities().stream()
            .map(MemberEntity::getId)
            .collect(Collectors.toList());

        return new PartyResponseDTO(
            partyEntity.getId(),
            partyEntity.getName(),
            partyEntity.isDeleted(),
            memberIds,
            partyEntity.getHostMemberId(),
            partyEntity.getEndDate(),
            partyEntity.isSameGenderOnly(),
            partyEntity.isCostShareBeforeDropOff(),
            partyEntity.isQuietMode(),
            partyEntity.isDestinationChangeIn5Minutes(),
            partyEntity.getStartDateTime(),
            partyEntity.getComment(),
            partyEntity.getCurrentParticipantCount(),
            partyEntity.getMaxParticipantCount(),
            startDto,
            endDto,
            partyEntity.getNotification(),
            partyEntity.isSavingsCalculated()
        );
    }

    public PartyEntity convertToEntity(PartyCreateRequestDTO createRequestDTO) {

        MapPlaceDTO sp = createRequestDTO.getStartPlace();
        MapPlaceDTO ep = createRequestDTO.getEndPlace();
        MapPlace startPlace = new MapPlace(sp.getName(), sp.getRoadAddressName(), sp.getX(),
            sp.getY());
        MapPlace endPlace = new MapPlace(ep.getName(), ep.getRoadAddressName(), ep.getX(),
            ep.getY());

        return new PartyEntity(
            null,
            createRequestDTO.isSameGenderOnly(),
            createRequestDTO.isCostShareBeforeDropOff(),
            createRequestDTO.isQuietMode(),
            createRequestDTO.isDestinationChangeIn5Minutes(),
            createRequestDTO.getStartDateTime(),
            createRequestDTO.getComment(),
            createRequestDTO.getCurrentParticipantCount(),
            createRequestDTO.getMaxParticipantCount(),
            startPlace,
            endPlace
        );
    }

    // 현재 인원수 필드 없음: 파티방에 멤버의 참여,퇴장은 join/leave 엔트포인트 사용의 강제를 위해
    public PartyEntity convertToEntityByUpdate(PartyEntity partyEntity,
        PartyUpdateRequestDTO partyUpdateRequestDTO) {
        MapPlaceDTO sp = partyUpdateRequestDTO.getStartPlace();
        MapPlaceDTO ep = partyUpdateRequestDTO.getEndPlace();
        MapPlace startPlace = new MapPlace(sp.getName(), sp.getRoadAddressName(), sp.getX(),
            sp.getY());
        MapPlace endPlace = new MapPlace(ep.getName(), ep.getRoadAddressName(), ep.getX(),
            ep.getY());

        return partyEntity.updateParty(
            partyUpdateRequestDTO.isSameGenderOnly(),
            partyUpdateRequestDTO.isCostShareBeforeDropOff(),
            partyUpdateRequestDTO.isQuietMode(),
            partyUpdateRequestDTO.isDestinationChangeIn5Minutes(),
            partyUpdateRequestDTO.getStartDateTime(),
            partyUpdateRequestDTO.getComment(),
            partyUpdateRequestDTO.getMaxParticipantCount(),
            startPlace,
            endPlace,
            partyUpdateRequestDTO.getNotification()
        );
    }
}
