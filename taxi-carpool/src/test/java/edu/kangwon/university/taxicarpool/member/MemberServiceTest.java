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
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 멤버_생성_성공() {
        // given
        MemberCreateDTO dto = new MemberCreateDTO(
            "test@kangwon.ac.kr",
            "testPassword12",
            "testNickname",
            Gender.MALE
        );

        when(passwordEncoder.encode("testPassword12")).thenReturn("encodedPassword12");
        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        MemberEntity savedEntity = new MemberEntity();
        savedEntity.setId(1L);
        savedEntity.setEmail(dto.getEmail());
        savedEntity.setNickname(dto.getNickname());
        savedEntity.setPassword("encodedPassword12");
        savedEntity.setGender(dto.getGender());

        when(memberRepository.save(any(MemberEntity.class))).thenReturn(savedEntity);

        // when
        MemberResponseDTO response = memberService.createMember(dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@kangwon.ac.kr");
        assertThat(response.getNickname()).isEqualTo("testNickname");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);

        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(passwordEncoder, times(1)).encode("testPassword12");
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void 중복된_이메일로_멤버_생성시_예외처리() {
        // given
        MemberCreateDTO dto = new MemberCreateDTO(
            "test@kangwon.ac.kr",
            "testPassword12",
            "testNickname",
            Gender.MALE
        );

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.createMember(dto))
            .isInstanceOf(DuplicatedEmailException.class)
            .hasMessageContaining("이미 사용 중인 이메일");

        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void 중복된_닉네임으로_멤버_생성시_예외처리() {
        // given
        MemberCreateDTO dto = new MemberCreateDTO(
            "test@kangwon.ac.kr",
            "testPassword12",
            "duplicateNickname",
            Gender.MALE
        );

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(memberRepository.existsByNickname(dto.getNickname())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.createMember(dto))
            .isInstanceOf(DuplicatedNicknameException.class)
            .hasMessageContaining("이미 사용 중인 닉네임");

        verify(memberRepository, times(1)).existsByEmail(dto.getEmail());
        verify(memberRepository, times(1)).existsByNickname(dto.getNickname());
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void 멤버_조회_성공() {
        // given
        Long memberId = 1L;
        MemberEntity entity = new MemberEntity();
        entity.setId(memberId);
        entity.setEmail("test@kangwon.ac.kr");
        entity.setNickname("testNickname");
        entity.setGender(Gender.MALE);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(entity));

        // when
        MemberResponseDTO response = memberService.getMember(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo("test@kangwon.ac.kr");
        assertThat(response.getNickname()).isEqualTo("testNickname");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);

        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void 존재하지_않는_멤버_조회시_예외처리() {
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
        existedEntity.setEmail("test@kangwon.ac.kr");
        existedEntity.setNickname("oldNickname");
        existedEntity.setPassword("oldPassword12");
        existedEntity.setGender(Gender.MALE);

        MemberUpdateDTO updateDto = new MemberUpdateDTO();
        updateDto.setNewNickname("newNickname");
        updateDto.setNewPassword("newPassword12");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existedEntity));
        when(passwordEncoder.encode("newPassword12")).thenReturn("encodedNewPassword12");

        MemberEntity updatedEntity = new MemberEntity();
        updatedEntity.setId(memberId);
        updatedEntity.setEmail("test@kangwon.ac.kr");
        updatedEntity.setNickname("newNickname");
        updatedEntity.setPassword("encodedNewPassword12");
        updatedEntity.setGender(Gender.MALE);

        when(memberRepository.save(any(MemberEntity.class))).thenReturn(updatedEntity);

        // when
        MemberResponseDTO response = memberService.updateMember(memberId, updateDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getNickname()).isEqualTo("newNickname");
        assertThat(response.getEmail()).isEqualTo("test@kangwon.ac.kr");

        verify(memberRepository, times(1)).findById(memberId);
        verify(passwordEncoder, times(1)).encode("newPassword12");
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void 존재하지_않는_멤버_수정시_예외처리() {
        // given
        Long memberId = 100L;
        MemberUpdateDTO updateDto = new MemberUpdateDTO();
        updateDto.setNewNickname("newNickname");
        updateDto.setNewPassword("newPassword12");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.updateMember(memberId, updateDto))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining("회원을 찾을 수 없습니다");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void 존재하는_닉네임으로_변경시_예외처리() {
        // given
        Long memberId = 1L;
        String oldNickname = "oldNickname";
        String newNickname = "duplicateNickname";

        // 이미 존재하는 회원
        MemberEntity existedEntity = new MemberEntity();
        existedEntity.setId(memberId);
        existedEntity.setEmail("test@kangwon.ac.kr");
        existedEntity.setNickname(oldNickname);
        existedEntity.setPassword("oldPassword12");
        existedEntity.setGender(Gender.MALE);

        MemberUpdateDTO updateDto = new MemberUpdateDTO();
        updateDto.setNewNickname(newNickname);
        updateDto.setNewPassword("newPassword12");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existedEntity));
        when(memberRepository.existsByNickname(newNickname)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.updateMember(memberId, updateDto))
            .isInstanceOf(DuplicatedNicknameException.class)
            .hasMessageContaining("이미 사용 중인 닉네임");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).existsByNickname(newNickname);
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void 멤버_삭제_성공() {
        // given
        Long memberId = 1L;
        MemberEntity existedEntity = new MemberEntity();
        existedEntity.setId(memberId);
        existedEntity.setEmail("test@kangwon.ac.kr");
        existedEntity.setNickname("testNickname");
        existedEntity.setPassword("testPassword12");
        existedEntity.setGender(Gender.MALE);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existedEntity));

        // when
        MemberResponseDTO response = memberService.deleteMember(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getEmail()).isEqualTo("test@kangwon.ac.kr");
        assertThat(response.getNickname()).isEqualTo("testNickname");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).delete(existedEntity);
    }

    @Test
    void 존재하지_않는_멤버_삭제시_예외처리() {
        // given
        Long memberId = 100L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.deleteMember(memberId))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining("회원을 찾을 수 없습니다");

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).delete(any(MemberEntity.class));
    }
}
