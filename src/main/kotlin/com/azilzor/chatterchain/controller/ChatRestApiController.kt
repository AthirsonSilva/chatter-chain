package com.azilzor.chatterchain.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = ["*"])
class ChatRestApiController {
    @GetMapping("/")
    fun index(): String {
        return "Greetings from Spring Boot!"
    }
}
