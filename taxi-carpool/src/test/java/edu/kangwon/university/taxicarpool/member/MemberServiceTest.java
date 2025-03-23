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
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import java.util.Optional;
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

    @Test
    void 멤버_조회_성공() {
        // given
        Long memberId = 1L;
        MemberEntity entity = new MemberEntity();
        entity.setId(memberId);
        entity.setEmail("test@example.com");
        entity.setNickname("testNickname");
        entity.setGender(Gender.MALE);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(entity));

        // when
        MemberResponseDTO response = memberService.getMember(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("testNickname");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);

        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void 존재하지_않는_멤버_조회시_에러() {
        // given
        Long memberId = 100L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getMember(memberId))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining("회원을 찾을 수 없습니다");

        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void 멤버_수정_성공() {
        // given
        Long memberId = 1L;
        MemberEntity existedEntity = new MemberEntity();
        existedEntity.setId(memberId);
        existedEntity.setEmail("test@example.com");
        existedEntity.setNickname("oldNickname");
        existedEntity.setPassword("oldPassword");
        existedEntity.setGender(Gender.MALE);

        MemberUpdateDTO updateDto = new MemberUpdateDTO();
        updateDto.setNewNickname("newNickname");
        updateDto.setNewPassword("newPassword");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existedEntity));

        MemberEntity updatedEntity = new MemberEntity();
        updatedEntity.setId(memberId);
        updatedEntity.setEmail("test@example.com");
        updatedEntity.setNickname("newNickname");
        updatedEntity.setPassword("newPassword");
        updatedEntity.setGender(Gender.MALE);

        when(memberRepository.save(any(MemberEntity.class))).thenReturn(updatedEntity);

        // when
        MemberResponseDTO response = memberService.updateMember(memberId, updateDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getNickname()).isEqualTo("newNickname");
        assertThat(response.getEmail()).isEqualTo("test@example.com");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void 존재하지_않는_멤버_수정시_에러() {
        // given
        Long memberId = 100L;
        MemberUpdateDTO updateDto = new MemberUpdateDTO();
        updateDto.setNewNickname("newNickname");
        updateDto.setNewPassword("newPassword");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.updateMember(memberId, updateDto))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining("회원을 찾을 수 없습니다");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }
}
