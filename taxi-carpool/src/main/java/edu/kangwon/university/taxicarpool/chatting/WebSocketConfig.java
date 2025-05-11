package edu.kangwon.university.taxicarpool.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 메시지 브로커가 지원하는 WebSocket 메시지 처리 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // HandShake와 통신을 담당할 Endpoint를 지정한다.
    // 클라이언트에서 서버와 WebSocket 연결을 하고 싶으면 "/stomp/chat"으로 요청을 보내도록 한다.
    // setAllowedOrigins는 cors를 위한 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat").setAllowedOriginPatterns("*");
    }

    // configureMessageBroker는 메모리 기반의 Simple Message Broker를 활성화 한다.
    // /sub으로 시작하는 주소의 Subscriber들에게 메시지 전달하는 역할을 한다.
    // 이때, 클라이언트가 서버로 메시지 보낼 때 붙어야 하는 prefix는 /pub으로 지정한다.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메시지를 받을 때, 경로를 설정해주는 함수
        // 내장 브로커를 사용하겠다는 설정
        // /sub가 api에 prefix로 붙은 경우, messageBroker가 해당 경로를 가로채 처리
        // 해당 경로 /sub으로 SimpleBroker를 등록한다.
        // SimpleBroker는 해당하는 경로로 구독하는 client에게 메시지를 전달하는 간단한 작업을 수행한다.
        registry.enableSimpleBroker("/sub");

        // 메시지를 보낼 때, 관련 경로를 설정해주는 함수
        // client에서 SEND 요청을 처리
        // 클라이언트가 메시지를 보낼 때, 경로 앞에 /pub가 붙어있으면 Broker로 보내진다.
        registry.setApplicationDestinationPrefixes("/pub");
    }
}