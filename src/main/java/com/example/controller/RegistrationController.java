package com.example.controller;

import com.example.domain.User;
import com.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user) {
        userRepository.findByUsername(user.getUsername());
        return "redirect:/login";
    }
}
