package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.Gender;
import edu.kangwon.university.taxicarpool.member.validation.EmailValid;
import edu.kangwon.university.taxicarpool.member.validation.NicknameValid;
import edu.kangwon.university.taxicarpool.member.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MemberCreateDTO {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @EmailValid
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @PasswordValid
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @NicknameValid
    private String nickname;

    @NotNull(message = "성별은 필수 선택 항목입니다.")
    private Gender gender;

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

