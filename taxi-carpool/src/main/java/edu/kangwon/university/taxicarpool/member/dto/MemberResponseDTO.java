package edu.kangwon.university.taxicarpool.member.dto;

import edu.kangwon.university.taxicarpool.member.Gender;

public class MemberResponseDTO {

    public MemberResponseDTO(Long id, String email, String nickname, Gender gender) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }

    private Long id;
    private String email;
    private String nickname;
    private Gender gender;

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
