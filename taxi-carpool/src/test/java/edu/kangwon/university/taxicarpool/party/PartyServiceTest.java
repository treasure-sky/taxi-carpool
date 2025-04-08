package edu.kangwon.university.taxicarpool.party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.PartyDTO.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private PartyMapper partyMapper;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PartyService partyService;

    private PartyEntity mockPartyEntity;
    private PartyCreateRequestDTO mockCreateDTO;
    private MemberEntity mockMemberEntity;

    @BeforeEach
    void setUp() {
        mockPartyEntity = new PartyEntity(
            "테스트 파티",
            new ArrayList<>(),
            100L,
            LocalDateTime.now().plusHours(2),
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "코멘트",
            1,
            4
        );

        mockCreateDTO = new PartyCreateRequestDTO(
            "테스트 파티",
            false,
            new ArrayList<>(),
            null, // hostMemberId(실제 createParty 로직에서 creatorMemberId로 대체)
            LocalDateTime.now().plusHours(2),
            100L, // creatorMemberId
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "코멘트",
            0,
            4
        );

        mockMemberEntity = new MemberEntity("test@test.com", "password", "닉네임",
            edu.kangwon.university.taxicarpool.member.Gender.MALE);
        mockMemberEntity.setEmail("test@test.com");
        mockMemberEntity.setNickname("테스트멤버");
        mockMemberEntity.setPassword("password");
    }

    @Test
    @DisplayName("getParty - 존재하지 않는 파티 조회 시 PartyNotFoundException")
    void testGetPartyNotFound() {
        // given
        when(partyRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PartyNotFoundException.class, () -> partyService.getParty(999L));
    }

    @Test
    @DisplayName("getParty - 파티 조회 성공")
    void testGetPartySuccess() {
        // given
        when(partyRepository.findById(1L)).thenReturn(Optional.of(mockPartyEntity));
        PartyResponseDTO mockResponse = new PartyResponseDTO(
            1L, "테스트 파티", false, new ArrayList<>(), 100L,
            LocalDateTime.now().plusHours(2), false, false, false, false,
            LocalDateTime.now(), "출발지", "도착지", "코멘트", 1, 4
        );
        when(partyMapper.convertToResponseDTO(mockPartyEntity)).thenReturn(mockResponse);

        // when
        PartyResponseDTO result = partyService.getParty(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("테스트 파티", result.getName());
    }

    @Test
    @DisplayName("createParty - creatorMemberId가 null이면 예외 발생")
    void testCreatePartyNoCreatorId() {
        // given
        PartyCreateRequestDTO dto = new PartyCreateRequestDTO(
            "테스트 파티",
            false,
            new ArrayList<>(),
            null,
            LocalDateTime.now().plusHours(2),
            null, // creatorMemberId = null
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            "출발지",
            "도착지",
            "코멘트",
            0,
            4
        );

        // when & then
        assertThrows(IllegalArgumentException.class, () -> partyService.createParty(dto));
    }

    @Test
    @DisplayName("createParty - 성공적으로 파티 생성")
    void testCreatePartySuccess() {
        // given
        when(memberRepository.findById(100L)).thenReturn(Optional.of(mockMemberEntity));
        when(partyMapper.convertToEntity(mockCreateDTO)).thenReturn(mockPartyEntity);
        when(partyRepository.save(mockPartyEntity)).thenReturn(mockPartyEntity);
        PartyResponseDTO mockResponse = new PartyResponseDTO(
            1L, "테스트 파티", false, new ArrayList<>(), 100L,
            LocalDateTime.now().plusHours(2), false, false, false, false,
            LocalDateTime.now(), "출발지", "도착지", "코멘트", 1, 4
        );
        when(partyMapper.convertToResponseDTO(mockPartyEntity)).thenReturn(mockResponse);

        // when
        PartyResponseDTO result = partyService.createParty(mockCreateDTO);

        // then
        assertNotNull(result);
        assertEquals("테스트 파티", result.getName());
        assertEquals(1, result.getCurrentParticipantCount());
    }

    @Test
    @DisplayName("joinParty - 이미 삭제된 파티면 예외 발생")
    void testJoinParty_DeletedParty() {
        // given
        mockPartyEntity.setDeleted(true);
        when(partyRepository.findById(1L)).thenReturn(Optional.of(mockPartyEntity));

        // when & then
        assertThrows(RuntimeException.class, () -> partyService.joinParty(1L, 100L));
    }

    @Test
    @DisplayName("joinParty - 멤버를 찾지 못하면 MemberNotFoundException")
    void testJoinParty_MemberNotFound() {
        // given
        when(partyRepository.findById(1L)).thenReturn(Optional.of(mockPartyEntity));
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> partyService.joinParty(1L, 999L));
    }

    // 기타 updateParty, leaveParty 등도 필요한 만큼 테스트 메서드를 추가해보세요.
}
