package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.validation.NicknameValid;
import edu.kangwon.university.taxicarpool.member.validation.PasswordValid;

public class MemberUpdateDTO {

    @PasswordValid
    private String newPassword;

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

    public boolean isEmpty() {
        return (newPassword == null || newPassword.isBlank()) &&
            (newNickname == null || newNickname.isBlank());
    }
}
