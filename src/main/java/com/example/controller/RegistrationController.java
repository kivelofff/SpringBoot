package com.example.controller;

import com.example.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user) {

        return "redirect:/login";
    }
}
