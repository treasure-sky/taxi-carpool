package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;
import org.springframework.web.util.UriComponentsBuilder;

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
        String token = extractToken(request);
        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        jwtUtil.validateToken(token);
        Long userId = jwtUtil.getIdFromToken(token);
        // STOMP의 모든 단계에서 범용적으로 사용할 수 있도록 Principal로 저장
        UsernamePasswordAuthenticationToken principal =
            new UsernamePasswordAuthenticationToken(userId, null, null);
        attributes.put("principal", principal);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Exception exception) {
        // 로직 상 딱히 할 일이 없음
    }

    private String extractToken(ServerHttpRequest request) {
        MultiValueMap<String, String> params =
            UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        List<String> tokens = params.get("access_token");
        if (tokens != null && !tokens.isEmpty()) {
            return tokens.get(0);
        }
        return null;
    }
}
