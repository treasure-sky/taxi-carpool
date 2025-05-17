package edu.kangwon.university.taxicarpool.chatting.dto;

public class NotificationResponseDTO {

    private Long partyId;
    private String notification;

    public NotificationResponseDTO(Long partyId, String notification) {
        this.partyId = partyId;
        this.notification = notification;
    }

    public Long getPartyId() {
        return partyId;
    }

    public String getNotification() {
        return notification;
    }

}
