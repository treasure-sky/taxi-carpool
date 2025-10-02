package edu.kangwon.university.taxicarpool.fcm;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import edu.kangwon.university.taxicarpool.fcm.dto.PushMessageDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmPushService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FcmTokenService fcmTokenService;
    private final FirebaseMessaging firebaseMessaging;

    /**
     * 특정 사용자에게 푸시 발송
     */
    public void sendPushToUser(Long userId, PushMessageDTO message) {
        List<FcmTokenEntity> tokens = fcmTokenRepository.findActiveTokensByUserId(userId);

        for (FcmTokenEntity token : tokens) {
            try {
                Message fcmMessage = buildFcmMessage(token, message);
                firebaseMessaging.send(fcmMessage);
            } catch (FirebaseMessagingException e) {
                fcmTokenService.handleFcmError(token.getFcmToken(), e.getMessagingErrorCode());
            }
        }
    }

    /**
     * 여러 사용자에게 푸시 발송
     */
    public void sendPushToUsers(List<Long> userIds, PushMessageDTO message) {
        for (Long userId : userIds) {
            sendPushToUser(userId, message);
        }
    }

    /**
     * 플랫폼별 FCM 메시지 빌드
     */
    private Message buildFcmMessage(FcmTokenEntity token, PushMessageDTO message) {
        Message.Builder messageBuilder = Message.builder()
            .setToken(token.getFcmToken())
            .setNotification(Notification.builder()
                .setTitle(message.getTitle())
                .setBody(message.getBody())
                .build())
            .putAllData(message.getData());

        // iOS/Android별 다른 설정
        if (token.getPlatform() == Platform.IOS) {
            messageBuilder.setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setSound("default")
                    .setBadge(1)
                    .build())
                .build());
        } else {
            messageBuilder.setAndroidConfig(AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                    .setChannelId("taxi_carpool_notifications")
                    .setDefaultSound(true)
                    .setPriority(AndroidNotification.Priority.HIGH)
                    .build())
                .build());
        }

        return messageBuilder.build();
    }

}
