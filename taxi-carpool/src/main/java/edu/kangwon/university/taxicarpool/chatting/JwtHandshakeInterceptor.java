package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

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
            // 세션 속성에 저장해둘 수도 있음
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
        List<String> auth = request.getHeaders().get("Authorization");
        if (auth != null && !auth.isEmpty() && auth.get(0).startsWith("Bearer ")) {
            return auth.get(0).substring(7);
        }
        // 필요하면 쿼리 파라미터에서도 꺼낼 수 있게 확장 가능
        return null;
    }
}
