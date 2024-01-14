package com.azilzor.chatterchain.controller;

import java.time.Instant;
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
import com.azilzor.chatterchain.dto.UserDto;
import com.azilzor.chatterchain.enums.Actions;
import com.azilzor.chatterchain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatWebSocketController {

  private final SimpMessagingTemplate messagingTemplate;
  private final UserService userService;

  @MessageMapping("/chat")
  public void processMessage(
      @Payload @NonNull MessageDto message,
      SimpMessageHeaderAccessor headerAccessor) {
    log.info("Received message: {}", message);
    messagingTemplate.convertAndSend("/topic/all/messages", message);

    if (Actions.JOINED.equals(message.action())) {
      if (message.user() == null) {
        log.error("Unable to get user from message because it is null");
        return;
      }

      log.info("User {} joined", message.user());

      String userDestination = String.format("/topic/%s/messages", message.user().id());

      // Send a message to all users that a new user has joined
      userService.getOnlineUsers().forEach(onlineUser -> {
        MessageDto newMessage = new MessageDto(onlineUser, null, Actions.JOINED, null);
        messagingTemplate.convertAndSend(userDestination, newMessage);
      });

      Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

      if (sessionAttributes == null) {
        log.error("Unable to get headerAccessor.getSessionAttributes() because it is null");
        return;
      }

      sessionAttributes.put("user", message.user());
      userService.addUser(message.user());
    }
  }

  @EventListener
  public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

    if (sessionAttributes == null) {
      log.error("Unable to get headerAccessor.getSessionAttributes() because it is null");
      return;
    }

    UserDto user = (UserDto) sessionAttributes.get("user");

    if (user == null) {
      log.error("Unable to get user from sessionAttributes because it is null");
      return;
    }

    userService.removeUser(user);

    MessageDto message = new MessageDto(user, "", Actions.LEFT, Instant.now());
    messagingTemplate.convertAndSend("/topic/all/messages", message);

    log.info("User {} disconnected", user);
  }

}
