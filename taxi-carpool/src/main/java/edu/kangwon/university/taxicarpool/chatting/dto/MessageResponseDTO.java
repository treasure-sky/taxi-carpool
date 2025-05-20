package edu.kangwon.university.taxicarpool.chatting.dto;

import edu.kangwon.university.taxicarpool.chatting.MessageType;
import java.time.LocalDateTime;

public class MessageResponseDTO {

    private final Long id;
    private final Long senderId;
    private final String senderNickname; // 프론트에서 이름을 알기 위해 추가적인 api 호출하는 것을 방지
    private final String content;
    private final LocalDateTime createdAt;
    private final MessageType type;

    public MessageResponseDTO(Long id, Long senderId, String senderNickname, String content,
        LocalDateTime createdAt, MessageType type) {
        this.id = id;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MessageType getType() {
        return type;
    }

}
