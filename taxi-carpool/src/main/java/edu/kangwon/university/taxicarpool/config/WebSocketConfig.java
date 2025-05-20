package edu.kangwon.university.taxicarpool.config;

import edu.kangwon.university.taxicarpool.chatting.JwtHandshakeInterceptor;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker // STOMP 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Autowired
    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // STOMP 메시지 브로커 설정
        registry.enableSimpleBroker("/sub"); // 구독
        // 1. 메시지 브로커 활성화
        // 2. 구독 경로 설정(앱->서버)
        // 3. 서버->앱 메시지 전송 경로
        registry.setApplicationDestinationPrefixes("/pub"); // 발행
        // 1. 앱->서버 메시지 전송 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 설정(이렇게만 해줘도 통신 터널을 열어두는 로직 반영됨)
        registry.addEndpoint("/chat")
            .addInterceptors(
                jwtHandshakeInterceptor) // http요청(웹소켓연결요청)에서 쿼리파라미터 내의 JWT토큰을 웹소켓 attribute에 저장 및 JWT토큰 검증
            .setHandshakeHandler(
                new DefaultHandshakeHandler() { // Principal을 반환하는 메서드. 웹소켓 세션에 해당 메서드 결과가 저장됨.
                    @Override
                    protected Principal determineUser(
                        ServerHttpRequest request,
                        WebSocketHandler wsHandler,
                        Map<String, Object> attributes
                        // jwtHandshakeInterceptor의 파라미터로 주어지는 attribute와 동일한 객체. 값이 공유됨.
                    ) {
                        // HandshakeInterceptor 에서 넣어둔 userId 꺼내서 Principal 반환
                        Long userId = (Long) attributes.get("userId");
                        // jwtHandshakeInterceptor에서 null 체크를 했으므로 바로 리턴
                        return new UsernamePasswordAuthenticationToken(
                            userId.toString(),    // name 으로 들어감 → principal.getName()
                            null,
                            Collections.emptyList()
                        );
                    }
                })
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (accessor.getUser() == null) {
                    throw new IllegalArgumentException("인증 정보 없음");
                }
                return message;
            }
        });
    }

}
