package edu.kangwon.university.taxicarpool.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    // 로그인 요청 DTO
    public static class LoginRequest {

        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;

        public LoginRequest() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    //로그인 응답 DTO
    public static class LoginResponse {

        private String token;       // JWT 액세스 토큰 (2시간)
        private String refreshToken; // 리프래쉬 토큰 (1주)
        private String email;       // 사용자 식별 정보(필요하면 추가로 더 담을 수 있음)

        public LoginResponse() {
        }

        // 일단 토큰이랑 이메일, 리프래쉬 토큰까지 리턴해주는 것으로 구현함.
        public LoginResponse(String token, String refreshToken, String email) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    //리프래쉬 토큰으로 액세스 토큰 재발급 요청 DTO
    public static class RefreshRequestDTO {

        private String refreshToken;

        public RefreshRequestDTO() {
        }

        public RefreshRequestDTO(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    /// 리프래쉬 토큰으로 액세스 토큰 재발급 응답 DTO
    public static class RefreshResponseDTO {

        private String newAccessToken;
        private String refreshToken;

        public RefreshResponseDTO() {
        }

        public RefreshResponseDTO(String newAccessToken, String refreshToken) {
            this.newAccessToken = newAccessToken;
            this.refreshToken = refreshToken;
        }

        public String getNewAccessToken() {
            return newAccessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
