package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.chatting.dto.MessageCreateDTO;
import edu.kangwon.university.taxicarpool.chatting.dto.MessageResponseDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChattingService chattingService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChattingService chattingService,
        SimpMessagingTemplate messagingTemplate) {
        this.chattingService = chattingService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 클라이언트→서버 메시지 발행을 처리하고,
     * 브로커로 브로드캐스트까지 수행.
     * destination: /pub/party/{partyId}/message
     */
    @MessageMapping("/party/{partyId}/message")
    public void sendMessage(@DestinationVariable Long partyId,
        @Payload MessageCreateDTO dto) {
        Long memberId = (Long) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        // 서비스에서 메시지 저장 + DTO 변환
        MessageResponseDTO response =
            chattingService.sendMessage(partyId, memberId, dto.getContent());

        // 브로커로 브로드캐스트 (SUBSCRIBE prefix: /sub)
        messagingTemplate.convertAndSend(
            "/sub/party/" + partyId,
            response
        );
    }
}
