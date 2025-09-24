package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.validation.NicknameValid;
import edu.kangwon.university.taxicarpool.member.validation.PasswordValid;
import edu.kangwon.university.taxicarpool.profanity.NoProfanity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateDTO {

    @Schema(description = "변경할 새 비밀번호 (닉네임이나 둘 중 하나는 필수)")
    @PasswordValid
    private String newPassword;

    @Schema(description = "변경할 새 닉네임 (비밀번호나 둘 중 하나는 필수)")
    @NicknameValid
    @NoProfanity(message = "닉네임에 비속어가 포함되어 있습니다.")
    private String newNickname;

    @Schema(hidden = true)
    public boolean isEmpty() {
        return (newPassword == null || newPassword.isBlank()) &&
            (newNickname == null || newNickname.isBlank());
    }
}
