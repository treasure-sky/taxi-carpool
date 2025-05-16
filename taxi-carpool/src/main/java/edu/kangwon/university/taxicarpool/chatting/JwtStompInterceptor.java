package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            String token = (authHeaders != null && !authHeaders.isEmpty())
                ? authHeaders.get(0).substring(7)
                : null;

            try {
                jwtUtil.validateToken(token);
            } catch (TokenExpiredException e) {
                // 새로운 메시지를 담은 예외를 던져서 CONNECT 자체를 거부
                throw new TokenExpiredException("WebSocket 연결 실패: Access Token이 만료되었습니다.", e);
            } catch (TokenInvalidException e) {
                throw new TokenInvalidException("WebSocket 연결 실패: 유효하지 않은 토큰입니다.", e);
            }

            Long userId = jwtUtil.getIdFromToken(token);
            accessor.setUser(
                new UsernamePasswordAuthenticationToken(userId, null, null)
            );
        }
        return message;
    }

}
