package edu.kangwon.university.taxicarpool.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LogoutDTO {

    public static class LogoutRequestDTO {

        @NotBlank(message = "재로그인 후 로그아웃 해주세요.") // 토큰 없는 로그아웃 요청
        private String refreshToken;

        public LogoutRequestDTO() {
        }

        public LogoutRequestDTO(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public @NotBlank(message = "재로그인 후 로그아웃 해주세요.") String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(
            @NotBlank(message = "재로그인 후 로그아웃 해주세요.") String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

}
