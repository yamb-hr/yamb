package com.tejko.yamb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.tejko.yamb.security.WebSocketAuthHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private WebSocketAuthHandler webSocketAuthHandler;

    @Autowired
    public WebSocketConfig(WebSocketAuthHandler webSocketAuthHandler) {
        this.webSocketAuthHandler = webSocketAuthHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/player");
    }

	@Value("${spring.profiles.active:default}")
	private String activeProfile;

    private final String[] DEV_ORIGINS = {
        "http://localhost:3000"
    };

    private final String[] PROD_ORIGINS = {
        "https://jamb.com.hr"
    };

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allowedOrigins = "dev".equalsIgnoreCase(activeProfile) ? DEV_ORIGINS : PROD_ORIGINS;
        registry.addEndpoint("/api/ws")
            .setAllowedOrigins(allowedOrigins)
            .setHandshakeHandler(webSocketAuthHandler)
            .withSockJS();
    }

}