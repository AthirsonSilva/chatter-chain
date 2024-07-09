package com.azilzor.chatterchain.controller

import com.azilzor.chatterchain.dto.MessageDto
import com.azilzor.chatterchain.service.ChatService
import com.azilzor.chatterchain.service.UserService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.lang.NonNull
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Controller
@RequiredArgsConstructor
@Log4j2
class ChatWebSocketController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val userService: UserService,
    private val chatService: ChatService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @MessageMapping("/chat")
    fun processMessage(
        @Payload @NonNull message: MessageDto, headerAccessor: SimpMessageHeaderAccessor
    ) {
        log.info("Received message: {}", message)

        val sessionAttributes = getSessionAttributes(headerAccessor)
        chatService.processIncomingMessage(message, sessionAttributes)
    }

    private fun getSessionAttributes(headerAccessor: SimpMessageHeaderAccessor): MutableMap<String?, Any?>? {
        val sessionAttributes = headerAccessor.sessionAttributes

        if (sessionAttributes == null) {
            log.error("Unable to get headerAccessor.getSessionAttributes() because it is null")
            return null
        }

        return sessionAttributes
    }

    @EventListener
    fun handleSessionDisconnectEvent(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionAttributes = headerAccessor.sessionAttributes
        chatService.proccessSessionDisconnect(sessionAttributes)
    }
}
