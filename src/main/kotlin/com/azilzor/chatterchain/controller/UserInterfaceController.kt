package com.azilzor.chatterchain.controller

import com.azilzor.chatterchain.security.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserInterfaceController {
    @GetMapping("/")
    fun index(model: Model, @AuthenticationPrincipal user: CustomUserDetails?): String {
        if (user == null) {
            return "redirect:login"
        }

        model.addAttribute("nickname", user.username)
        return "index"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }
}
