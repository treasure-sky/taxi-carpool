package edu.kangwon.university.taxicarpool.party;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PartyEntityTest {

    @Autowired
    private PartyRepository partyRepository;

    @Test
    @DisplayName("PartyEntity를 DB에 저장 후 조회 테스트")
    void testSaveAndFindPartyEntity() {
        // given
        PartyEntity party = new PartyEntity(
            "테스트 파티",
            new ArrayList<>(),
            null, // hostMemberId
            LocalDateTime.now().plusHours(1),
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "테스트 코멘트",
            1,
            4
        );

        // when
        PartyEntity saved = partyRepository.save(party);
        // then
        assertNotNull(saved.getId());
        assertEquals("테스트 파티", saved.getName());
        assertFalse(saved.isDeleted());
        assertEquals(1, saved.getCurrentParticipantCount());
        assertEquals(4, saved.getMaxParticipantCount());
    }
}
