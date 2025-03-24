package edu.kangwon.university.taxicarpool.auth;

public class LogoutDTO {

    public static class LogoutRequestDTO {
        private String email;

        public LogoutRequestDTO() {}
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
