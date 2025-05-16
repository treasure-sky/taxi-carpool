package edu.kangwon.university.taxicarpool.chatting.dto;

public class ParticipantResponseDTO {
    private Long memberId;
    private String nickname;

    public ParticipantResponseDTO(Long memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNickname() {
        return nickname;
    }
}
