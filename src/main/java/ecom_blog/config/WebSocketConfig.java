package ecom_blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration WebSocket pour le suivi en temps réel des véhicules
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Activer un simple broker de messages pour diffuser aux clients
        // /topic pour les diffusions publiques (positions véhicules)
        // /queue pour les messages privés (notifications chauffeur)
        config.enableSimpleBroker("/topic", "/queue");

        // Préfixe pour les messages venant des clients vers le serveur
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point de connexion WebSocket avec fallback SockJS
        registry.addEndpoint("/ws-tracking")
                .setAllowedOriginPatterns("*") // En prod, restreindre aux origines de confiance
                .withSockJS();
    }
}
