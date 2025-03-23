package edu.kangwon.university.taxicarpool.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 멤버_생성_성공() {
        // given
        MemberCreateDTO dto = new MemberCreateDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("testPassword");
        dto.setNickname("testNickname");
        dto.setGender(Gender.MALE);

        // 반환될 entity
        MemberEntity savedEntity = new MemberEntity();
        savedEntity.setId(1L);
        savedEntity.setEmail(dto.getEmail());
        savedEntity.setNickname(dto.getNickname());
        savedEntity.setPassword(dto.getPassword());
        savedEntity.setGender(dto.getGender());

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(savedEntity);

        // when
        MemberResponseDTO response = memberService.createMember(dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("testNickname");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);

        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void 중복된_이메일로_멤버_생성시_에러() {
        // given
        MemberCreateDTO dto = new MemberCreateDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("testPassword");
        dto.setNickname("testNickname");
        dto.setGender(Gender.MALE);

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.createMember(dto))
            .isInstanceOf(DuplicatedEmailException.class)
            .hasMessageContaining("이미 사용 중인 이메일");

        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

}
