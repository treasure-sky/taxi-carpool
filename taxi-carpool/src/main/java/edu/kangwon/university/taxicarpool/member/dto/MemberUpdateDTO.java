package edu.kangwon.university.taxicarpool.member.dto;

public class MemberUpdateDTO {

    private String newPassword;
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

