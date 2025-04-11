package edu.kangwon.university.taxicarpool.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
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
    void 멤버_조회_성공() throws Exception {
        // given
        Long memberId = 1L;

        MemberResponseDTO responseDTO = new MemberResponseDTO(
            memberId,
            "test@kangwon.ac.kr",
            "testNickname",
            Gender.MALE
        );

        when(memberService.getMemberById(memberId)).thenReturn(responseDTO);

        // when & then
        mockMvc.perform(get("/api/member/{id}", memberId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.email").value("test@kangwon.ac.kr"))
            .andExpect(jsonPath("$.nickname").value("testNickname"))
            .andExpect(jsonPath("$.gender").value("MALE"));

        verify(memberService, times(1)).getMemberById(memberId);
    }

    @Test
    void 존재하지_않는_멤버_조회시_예외처리() throws Exception {
        // given
        Long memberId = 100L;
        when(memberService.getMemberById(memberId))
            .thenThrow(new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/api/member/{id}", memberId))
            .andExpect(status().isNotFound());

        verify(memberService, times(1)).getMemberById(memberId);
    }

    @Test
    void 멤버_수정_성공() throws Exception {
        // given
        Long memberId = 1L;
        MemberUpdateDTO updateDTO = new MemberUpdateDTO();
        updateDTO.setNewNickname("newNickname");
        updateDTO.setNewPassword("newPassword12");

        MemberResponseDTO responseDTO = new MemberResponseDTO(
            memberId,
            "test@kangwon.ac.kr",
            "newNickname",
            Gender.MALE
        );

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
            .andExpect(jsonPath("$.email").value("test@kangwon.ac.kr"))
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
        updateDTO.setNewPassword("newPassword12");

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
        MemberResponseDTO responseDTO = new MemberResponseDTO(
            memberId,
            "test@kangwon.ac.kr",
            "testNickname",
            Gender.MALE
        );

        when(memberService.deleteMember(memberId)).thenReturn(responseDTO);

        // when & then
        mockMvc.perform(delete("/api/member/{id}", memberId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.nickname").value("testNickname"))
            .andExpect(jsonPath("$.email").value("test@kangwon.ac.kr"))
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
