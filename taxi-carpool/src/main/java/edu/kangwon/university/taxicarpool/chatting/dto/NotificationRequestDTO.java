package edu.kangwon.university.taxicarpool.chatting.dto;

import edu.kangwon.university.taxicarpool.profanity.NoProfanity;

public class NotificationRequestDTO {

    @NoProfanity(message = "공지사항에 비속어가 포함되어 있습니다.")
    private String notification;

    public NotificationRequestDTO() {
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

}