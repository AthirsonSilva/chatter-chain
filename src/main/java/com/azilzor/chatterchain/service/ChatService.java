package com.azilzor.chatterchain.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.azilzor.chatterchain.dto.MessageDto;
import com.azilzor.chatterchain.dto.UserDto;
import com.azilzor.chatterchain.enums.Actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

  private final SimpMessagingTemplate messagingTemplate;
  private final UserService userService;

  public void processIncomingMessage(MessageDto message, Map<String, Object> sessionAttributes) {
    messagingTemplate.convertAndSend("/topic/all/messages", message);

    if (Actions.JOINED.equals(message.action())) {
      if (message.user() == null) {
        log.error("Unable to get user from message because it is null");
        return;
      }

      log.info("User {} joined", message.user());
      String userDestination = String.format("/topic/%s/messages", message.user().id());
      userService
          .getOnlineUsers()
          .forEach(
              onlineUser -> {
                MessageDto newMessage = new MessageDto(onlineUser, null, Actions.JOINED, null);
                messagingTemplate.convertAndSend(userDestination, newMessage);
              });

      sessionAttributes.put("user", message.user());
      userService.addUser(message.user());
    }
  }

  public void proccessSessionDisconnect(Map<String, Object> sessionAttributes) {

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
