package edu.kangwon.university.taxicarpool.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LogoutDTO {

    public static class LogoutRequestDTO {

        @NotBlank
        @Email
        private String email;

        public LogoutRequestDTO() {
        }

        public LogoutRequestDTO(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
