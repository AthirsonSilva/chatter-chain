package com.azilzor.chatterchain.dto

import com.azilzor.chatterchain.enums.Actions
import java.time.Instant

data class MessageDto(val user: UserDto?, val comment: String?, val action: Actions, val timestamp: Instant?)
