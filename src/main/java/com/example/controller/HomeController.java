package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Login was not successful");
        }
        
        return "index";
    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String name = principal.getAttribute("given_name");
        String surname = principal.getAttribute("family_name");
        
        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        
        return "account";
    }
}
