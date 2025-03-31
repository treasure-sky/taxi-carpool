package edu.kangwon.university.taxicarpool.email.dto;

import edu.kangwon.university.taxicarpool.member.validation.EmailValid;
import jakarta.validation.constraints.NotNull;

public class EmailVerifyRequest {

    @NotNull
    @EmailValid
    private String email;
    
    @NotNull
    private String code;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
