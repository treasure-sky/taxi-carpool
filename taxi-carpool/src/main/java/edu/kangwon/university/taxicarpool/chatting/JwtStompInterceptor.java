package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class JwtStompInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtStompInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        log.debug("STOMP preSend ▸ sessionId={}, command={}, user={}",
            accessor.getSessionId(), command, accessor.getUser());

        // CONNECT
        if (StompCommand.CONNECT.equals(command)) {
            if (accessor.getUser() == null) {
                log.error("STOMP CONNECT 실패 – user가 없습니다.");
                throw new TokenInvalidException("WebSocket 연결 실패: 인증 정보가 없습니다.");
            }
        }
        // SUBSCRIBE & SEND
        else if (StompCommand.SUBSCRIBE.equals(command) || StompCommand.SEND.equals(command)) {
            if (accessor.getUser() == null) {
                log.error("STOMP {} 실패 – user가 없습니다.", command);
                throw new TokenInvalidException("WebSocket 연결 실패: 인증 정보가 없습니다.");
            }
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

}