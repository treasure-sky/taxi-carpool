package edu.kangwon.university.taxicarpool.email.dto;

import edu.kangwon.university.taxicarpool.member.validation.EmailValid;
import jakarta.validation.constraints.NotNull;

public class EmailSendRequest {

    @NotNull
    @EmailValid
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

