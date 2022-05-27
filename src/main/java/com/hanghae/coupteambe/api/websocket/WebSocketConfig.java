package com.hanghae.coupteambe.api.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @Value("${develop.url}")
    private String DEVELOP_URL;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")

                .setAllowedOrigins(FRONTEND_URL, DEVELOP_URL, "http://localhost:8080");
        registry.addEndpoint("/ws")
                .setAllowedOrigins(FRONTEND_URL, DEVELOP_URL, "http://localhost:8080").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(1024 * 1024);
        registry.setSendBufferSizeLimit(1024 * 1024);
    }
}
