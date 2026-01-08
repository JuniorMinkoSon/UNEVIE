package ecom_blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig - Configuration WebSocket pour communication temps réel
 * Utilisé pour le tracking GPS et les notifications
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Préfixe pour les messages diffusés aux clients
        config.enableSimpleBroker("/topic", "/queue");

        // Préfixe pour les messages envoyés par les clients
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint WebSocket avec fallback SockJS
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // À restreindre en production
                .withSockJS();
    }
}
