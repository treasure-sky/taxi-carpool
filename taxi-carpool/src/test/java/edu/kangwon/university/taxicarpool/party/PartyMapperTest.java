package edu.kangwon.university.taxicarpool.party;

import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PartyMapperTest {

    private final PartyMapper partyMapper = new PartyMapper();

    @Test
    @DisplayName("PartyCreateRequestDTO -> PartyEntity 변환 테스트")
    void testConvertToEntity() {
        // given
        PartyCreateRequestDTO createDTO = new PartyCreateRequestDTO(
            "테스트파티",
            false,
            new ArrayList<>(),
            100L,
            LocalDateTime.now().plusHours(2),
            999L, // creatorMemberId
            true,
            true,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "코멘트",
            1,
            4
        );

        // when
        PartyEntity entity = partyMapper.convertToEntity(createDTO);

        // then
        assertEquals(createDTO.getName(), entity.getName());
        assertEquals(createDTO.getHostMemberId(), entity.getHostMemberId());
        assertEquals(createDTO.getEndDate(), entity.getEndDate());
        assertTrue(entity.isSameGenderOnly());
        assertTrue(entity.isCostShareBeforeDropOff());
        assertFalse(entity.isQuietMode());
        assertFalse(entity.isDestinationChangeIn5Minutes());
        assertEquals(createDTO.getStartDateTime(), entity.getStartDateTime());
        assertEquals(createDTO.getStartLocation(), entity.getStartLocation());
        assertEquals(createDTO.getEndLocation(), entity.getEndLocation());
        assertEquals(createDTO.getComment(), entity.getComment());
        assertEquals(createDTO.getCurrentParticipantCount(), entity.getCurrentParticipantCount());
        assertEquals(createDTO.getMaxParticipantCount(), entity.getMaxParticipantCount());
    }

    @Test
    @DisplayName("PartyEntity -> PartyResponseDTO 변환 테스트")
    void testConvertToResponseDTO() {
        // given
        PartyEntity entity = new PartyEntity(
            "테스트파티",
            new ArrayList<>(),
            100L,
            LocalDateTime.now().plusHours(2),
            true,
            false,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "코멘트",
            2,
            4
        );
        entity.setHostMemberId(100L);

        // when
        PartyResponseDTO responseDTO = partyMapper.convertToResponseDTO(entity);

        // then
        assertEquals(entity.getId(), responseDTO.getId());
        assertEquals(entity.getName(), responseDTO.getName());
        assertEquals(entity.isDeleted(), responseDTO.isDeleted());
        assertEquals(entity.getMemberEntities(), responseDTO.getMemberEntities());
        assertEquals(entity.getHostMemberId(), responseDTO.getHostMemberId());
        assertEquals(entity.isSameGenderOnly(), responseDTO.isSameGenderOnly());
        assertEquals(entity.isCostShareBeforeDropOff(), responseDTO.isCostShareBeforeDropOff());
        assertEquals(entity.isQuietMode(), responseDTO.isQuietMode());
        assertEquals(entity.isDestinationChangeIn5Minutes(), responseDTO.isDestinationChangeIn5Minutes());
        assertEquals(entity.getStartDateTime(), responseDTO.getStartDateTime());
        assertEquals(entity.getStartLocation(), responseDTO.getStartLocation());
        assertEquals(entity.getEndLocation(), responseDTO.getEndLocation());
        assertEquals(entity.getComment(), responseDTO.getComment());
        assertEquals(entity.getCurrentParticipantCount(), responseDTO.getCurrentParticipantCount());
        assertEquals(entity.getMaxParticipantCount(), responseDTO.getMaxParticipantCount());
    }
}
