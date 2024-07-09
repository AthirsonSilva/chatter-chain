package com.azilzor.chatterchain.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.lang.NonNull
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(@NonNull registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic")
    }

    override fun registerStompEndpoints(@NonNull registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/websocket")
            .withSockJS()
    }
}
