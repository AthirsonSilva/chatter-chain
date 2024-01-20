package com.azilzor.chatterchain.controller;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.azilzor.chatterchain.dto.MessageDto;
import com.azilzor.chatterchain.service.ChatService;
import com.azilzor.chatterchain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatWebSocketController {

  private final SimpMessagingTemplate messagingTemplate;
  private final UserService userService;
  private final ChatService chatService;

  @MessageMapping("/chat")
  public void processMessage(
      @Payload @NonNull MessageDto message, SimpMessageHeaderAccessor headerAccessor) {
    log.info("Received message: {}", message);
    Map<String, Object> sessionAttributes = getSessionAttributes(headerAccessor);
    chatService.processIncomingMessage(message, sessionAttributes);
  }

  private Map<String, Object> getSessionAttributes(SimpMessageHeaderAccessor headerAccessor) {
    Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

    if (sessionAttributes == null) {
      log.error("Unable to get headerAccessor.getSessionAttributes() because it is null");
      return null;
    }

    return sessionAttributes;
  }

  @EventListener
  public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
    chatService.proccessSessionDisconnect(sessionAttributes);
  }
}
