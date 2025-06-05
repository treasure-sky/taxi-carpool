package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtStompInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtStompInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        // CONNECT 요청
        if (StompCommand.CONNECT.equals(command)) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            if (authHeaders == null || authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
                throw new TokenInvalidException("WebSocket 연결 실패: 토큰이 없습니다.");
            }
            String token = authHeaders.get(0).substring(7);
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.getIdFromToken(token);

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, null, null);
            accessor.setUser(authToken);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        // SUBSCRIBE나 SEND 요청일 때도 user가 없으면 차단
        else if (StompCommand.SUBSCRIBE.equals(command) || StompCommand.SEND.equals(command)) {
            if (accessor.getUser() == null) {
                throw new TokenInvalidException("WebSocket 연결 실패: 토큰이 없습니다.");
            }
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

}
