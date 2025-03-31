package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.member.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignUpDTO {

    public static class SignUpRequestDTO {

        @NotBlank
        @Email // merge후에 진호가 만든 validation으로 바꾸기
        private String email;

        @NotBlank
        private String password;

        // Blank일 경우 랜덤이어야함.
        private String nickname;

        @NotNull
        private Gender gender;

        public SignUpRequestDTO() {
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }
    }

    public static class SignUpResponseDTO {

        private Long id;
        private String email;
        private String nickname;
        private Gender gender;

        public SignUpResponseDTO() {
        }

        public SignUpResponseDTO(Long id, String email, String nickname, Gender gender) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }
    }

}
