package com.azilzor.chatterchain.service

import com.azilzor.chatterchain.dto.MessageDto
import com.azilzor.chatterchain.dto.UserDto
import com.azilzor.chatterchain.enums.Actions
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.function.Consumer

@Log4j2
@Service
@RequiredArgsConstructor
class ChatService(
    private val messagingTemplate: SimpMessagingTemplate,
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun processIncomingMessage(message: MessageDto, sessionAttributes: MutableMap<String?, Any?>?) {
        messagingTemplate.convertAndSend("/topic/all/messages", message)

        if (Actions.JOINED == message.action) {
            if (message.user == null) {
                log.error("Unable to get user from message because it is null")
                return
            }

            log.info("User {} joined", message.user)
            val userDestination = String.format("/topic/%s/messages", message.user.id)
            userService
                .getOnlineUsers()
                .forEach(
                    Consumer { onlineUser: UserDto? ->
                        val newMessage = MessageDto(onlineUser, null, Actions.JOINED, null)
                        messagingTemplate.convertAndSend(userDestination, newMessage)
                    })

            sessionAttributes!!["user"] = message.user
            userService.addUser(message.user)
        }
    }

    fun proccessSessionDisconnect(sessionAttributes: Map<String?, Any?>?) {
        val user = sessionAttributes!!["user"] as UserDto?

        if (user == null) {
            log.error("Unable to get user from sessionAttributes because it is null")
            return
        }

        userService.removeUser(user)

        val message = MessageDto(user, "", Actions.LEFT, Instant.now())
        messagingTemplate.convertAndSend("/topic/all/messages", message)

        log.info("User {} disconnected", user)
    }
}
