package edu.kangwon.university.taxicarpool.auth.reset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetDTO {

    public static class SendLinkRequest {
        @Schema(description = "비번 재설정 링크를 받을 이메일")
        @NotBlank @Email
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class ResetRequest {
        @Schema(description = "이메일 링크에 포함된 1회용 토큰")
        @NotBlank
        private String token;

        @Schema(description = "새 비밀번호")
        @NotBlank
        private String newPassword;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
