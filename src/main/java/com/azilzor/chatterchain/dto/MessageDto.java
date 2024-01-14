package com.azilzor.chatterchain.dto;

import java.time.Instant;

import com.azilzor.chatterchain.enums.Actions;

public record MessageDto(UserDto user, String comment, Actions action, Instant timestamp) {

}
