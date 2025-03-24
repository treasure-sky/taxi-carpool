package edu.kangwon.university.taxicarpool.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 멤버_생성_성공() throws Exception {
        // given
        MemberCreateDTO createDTO = new MemberCreateDTO();
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("testPassword");
        createDTO.setNickname("testNickname");
        createDTO.setGender(Gender.MALE);

        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail(createDTO.getEmail());
        responseDTO.setNickname(createDTO.getNickname());
        responseDTO.setGender(createDTO.getGender());

        when(memberService.createMember(any(MemberCreateDTO.class))).thenReturn(responseDTO);

        // JSON으로 변환
        String requestBody = objectMapper.writeValueAsString(createDTO);

        // when & then
        mockMvc.perform(
                post("/api/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.nickname").value("testNickname"))
            .andExpect(jsonPath("$.gender").value("MALE"));

        verify(memberService, times(1)).createMember(any(MemberCreateDTO.class));
    }

    @Test
    void 중복된_이메일로_멤버_생성시_예외처리() throws Exception {
        // given
        MemberCreateDTO createDTO = new MemberCreateDTO();
        createDTO.setEmail("duplicate@example.com");
        createDTO.setPassword("testPassword");
        createDTO.setNickname("testNickname");
        createDTO.setGender(Gender.MALE);

        when(memberService.createMember(any(MemberCreateDTO.class)))
            .thenThrow(new DuplicatedEmailException("이미 사용 중인 이메일입니다."));

        String requestBody = objectMapper.writeValueAsString(createDTO);

        // when & then
        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isConflict());

        verify(memberService, times(1)).createMember(any(MemberCreateDTO.class));
    }

    @Test
    void 중복된_닉네임으로_멤버_생성시_예외처리() throws Exception {
        // given
        MemberCreateDTO createDTO = new MemberCreateDTO();
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("testPassword");
        createDTO.setNickname("duplicatedNickname");
        createDTO.setGender(Gender.MALE);

        when(memberService.createMember(any(MemberCreateDTO.class)))
            .thenThrow(new DuplicatedNicknameException("이미 사용 중인 닉네임입니다."));

        String requestBody = objectMapper.writeValueAsString(createDTO);

        // when & then
        mockMvc.perform(
                post("/api/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isConflict());

        verify(memberService, times(1)).createMember(any(MemberCreateDTO.class));
    }

    @Test
    void 멤버_조회_성공() throws Exception {
        // given
        Long memberId = 1L;

        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(memberId);
        responseDTO.setEmail("test@example.com");
        responseDTO.setNickname("testNickname");
        responseDTO.setGender(Gender.MALE);

        when(memberService.getMember(memberId)).thenReturn(responseDTO);

        // when & then
        mockMvc.perform(get("/api/member/{id}", memberId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.nickname").value("testNickname"))
            .andExpect(jsonPath("$.gender").value("MALE"));

        verify(memberService, times(1)).getMember(memberId);
    }

    @Test
    void 존재하지_않는_멤버_조회시_예외처리() throws Exception {
        // given
        Long memberId = 100L;
        when(memberService.getMember(memberId))
            .thenThrow(new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/api/member/{id}", memberId))
            .andExpect(status().isNotFound());

        verify(memberService, times(1)).getMember(memberId);
    }

    @Test
    void 멤버_수정_성공() throws Exception {
        // given
        Long memberId = 1L;
        MemberUpdateDTO updateDTO = new MemberUpdateDTO();
        updateDTO.setNewNickname("newNickname");
        updateDTO.setNewPassword("newPassword");

        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(memberId);
        responseDTO.setEmail("test@example.com");
        responseDTO.setNickname("newNickname");
        responseDTO.setGender(Gender.MALE);

        when(memberService.updateMember(eq(memberId), any(MemberUpdateDTO.class)))
            .thenReturn(responseDTO);

        String requestBody = objectMapper.writeValueAsString(updateDTO);

        // when & then
        mockMvc.perform(
                put("/api/member/{id}", memberId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.nickname").value("newNickname"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.gender").value("MALE"));

        verify(memberService, times(1)).updateMember(eq(memberId), any(MemberUpdateDTO.class));
    }

    @Test
    @DisplayName("회원 수정 실패 - 존재하지 않는 회원")
    void 존재하지_않는_멤버_수정시_예외처리() throws Exception {
        // given
        Long memberId = 100L;
        MemberUpdateDTO updateDTO = new MemberUpdateDTO();
        updateDTO.setNewNickname("newNickname");
        updateDTO.setNewPassword("newPassword");

        when(memberService.updateMember(eq(memberId), any(MemberUpdateDTO.class)))
            .thenThrow(new MemberNotFoundException("회원을 찾을 수 없습니다"));

        String requestBody = objectMapper.writeValueAsString(updateDTO);

        // when & then
        mockMvc.perform(
                put("/api/member/{id}", memberId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isNotFound());

        verify(memberService, times(1)).updateMember(eq(memberId), any(MemberUpdateDTO.class));
    }

    @Test
    void 멤버_삭제_성공() throws Exception {
        // given
        Long memberId = 1L;
        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(memberId);
        responseDTO.setEmail("test@example.com");
        responseDTO.setNickname("testNickname");
        responseDTO.setGender(Gender.MALE);

        when(memberService.deleteMember(memberId)).thenReturn(responseDTO);

        // when & then
        mockMvc.perform(delete("/api/member/{id}", memberId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.nickname").value("testNickname"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.gender").value("MALE"));

        verify(memberService, times(1)).deleteMember(memberId);
    }

    @Test
    @DisplayName("회원 삭제 실패 - 존재하지 않는 회원")
    void 존재하지_않는_멤버_삭제시_예외처리() throws Exception {
        // given
        Long memberId = 100L;
        when(memberService.deleteMember(memberId))
            .thenThrow(new MemberNotFoundException("회원을 찾을 수 없습니다"));

        // when & then
        mockMvc.perform(delete("/api/member/{id}", memberId))
            .andExpect(status().isNotFound());

        verify(memberService, times(1)).deleteMember(memberId);
    }
}
