package edu.kangwon.university.taxicarpool.chatting.dto;

import edu.kangwon.university.taxicarpool.chatting.MessageType;
import java.time.LocalDateTime;

public class MessageResponseDTO {

    private Long id;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;
    private MessageType type;

    public MessageResponseDTO(Long id, Long senderId, String content,
        LocalDateTime createdAt, MessageType type) {
        this.id = id;
        this.senderId = senderId;
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
