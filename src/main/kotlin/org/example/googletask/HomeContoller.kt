package org.example.googletask

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeContoller {

    @GetMapping("/")
    fun home(): String = "index"

    @GetMapping("/welcome")
    fun welcome(model: Model, @AuthenticationPrincipal oauthUser: OAuth2User): String {
        val firstName = oauthUser.getAttribute<String>("given_name") ?: "User"
        val lastName = oauthUser.getAttribute<String>("family_name") ?: ""
        model.addAttribute("user", "$firstName $lastName")
        return "welcome"
    }
}