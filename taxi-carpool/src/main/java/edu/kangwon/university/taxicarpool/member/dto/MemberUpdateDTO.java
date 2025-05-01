package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.validation.NicknameValid;
import edu.kangwon.university.taxicarpool.member.validation.PasswordValid;
import io.swagger.v3.oas.annotations.media.Schema;

public class MemberUpdateDTO {

    @Schema(description = "변경할 새 비밀번호 (닉네임이나 둘 중 하나는 필수)")
    @PasswordValid
    private String newPassword;

    @Schema(description = "변경할 새 닉네임 (비밀번호나 둘 중 하나는 필수)")
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
