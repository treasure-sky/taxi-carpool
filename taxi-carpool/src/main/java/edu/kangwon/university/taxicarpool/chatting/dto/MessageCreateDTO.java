package edu.kangwon.university.taxicarpool.chatting.dto;

public class MessageCreateDTO {

    // 사용자의 id는 토큰에서 추출
    // 사용자의 메세지 생성은 하나의 타입만 가지므로 타입도 받지 않음

    private String content;

    public MessageCreateDTO() {
    }

    public MessageCreateDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
