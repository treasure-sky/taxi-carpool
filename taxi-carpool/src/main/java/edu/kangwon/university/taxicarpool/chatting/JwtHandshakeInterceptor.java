package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes) {
        // HTTP 헤더나 쿼리에서 토큰 추출
        String token = extractToken(request);
        if (token == null) {
            // 토큰이 없으면 401 응답 후 핸드셰이크 차단
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        try {
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.getIdFromToken(token);
            // 웹소켓 세션 attribute에 저장해두기
            attributes.put("userId", userId);
            return true;
        } catch (TokenExpiredException e) {
            // 토큰 만료 시, 핸드셰이크 차단
            throw new TokenExpiredException("액세스 토큰이 만료되었습니다.");
        } catch (TokenInvalidException ex) {
            // 서명 불일치 등 잘못된 토큰
            throw new TokenInvalidException("액세스 토큰이 유효하지 않습니다.");
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Exception exception) {
        // no-op
        // 로직 상 딱히 할 일이 없음. 인터페이스 구현 의무사항만 채움.
    }

    private String extractToken(ServerHttpRequest request) {
        // 프론트에서 SockJS()와 같은 메서드를 사용시 헤더추가 불가능
        // -> 헤더가 아닌 쿼리스트링으로 토큰 전달

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest =
                ((ServletServerHttpRequest) request).getServletRequest();
            String tokenFromQuery = servletRequest.getParameter(
                "token"); // 클라이언트에서 "chat?token=asf2fa..." 이런식으로 전송해야 함.
            if (tokenFromQuery != null && !tokenFromQuery.isEmpty()) {
                return tokenFromQuery;
            }
        }
        return null;
    }
}
