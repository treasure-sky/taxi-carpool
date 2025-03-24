package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.validation.NicknameValid;
import edu.kangwon.university.taxicarpool.member.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;

public class MemberUpdateDTO {

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @PasswordValid
    private String newPassword;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @NicknameValid
    private String newNickname;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }
}

