package com.azilzor.chatterchain.service

import com.azilzor.chatterchain.dto.UserDto
import org.springframework.stereotype.Service

@Service
class UserService {
    private val onlineUsers: MutableSet<UserDto?> = LinkedHashSet()

    fun getOnlineUsers(): Set<UserDto?> {
        return onlineUsers
    }

    fun addUser(user: UserDto?) {
        onlineUsers.add(user)
    }

    fun removeUser(user: UserDto?) {
        onlineUsers.remove(user)
    }
}
