package com.azilzor.chatterchain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class User {
    @Id
    private val id: String? = null
    private val nickname: String? = null
    private val avatarID: String? = null
    private val createdAt: LocalDateTime = LocalDateTime.now()
    private val updatedAt: LocalDateTime = LocalDateTime.now()
}
