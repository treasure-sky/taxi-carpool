package edu.kangwon.university.taxicarpool.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;

        public LoginRequest() {}
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

    public static class LoginResponse {
        private String token;  // JWT 토큰
        private String email;  // 사용자 식별 정보(선택)
        // 필요한 정보를 추가로 담을 수 있음 (예: 닉네임, 권한 등) 근데 필요 없을듯.

        public LoginResponse() {}
        public LoginResponse(String token, String email) {
            this.token = token;
            this.email = email;
        }

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
