package com.example.controller;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repos.MessasgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
        @Autowired
        private MessasgeRepository messasgeRepository;

        @GetMapping("/")
        public String greeting() {

            return "greeting";
        }

        @GetMapping("/main")
        public String main(@RequestParam(required = false) String filter, Model model) {
            Iterable<Message> messages = messasgeRepository.findAll();
            if (filter != null && !filter.isEmpty()) {
                messages = messasgeRepository.findByTag(filter);
            } else {
                messages = messasgeRepository.findAll();
            }
            model.addAttribute("messages", messages);
            model.addAttribute("filter", filter);
            return "main";
        }

        @PostMapping("/main")
        public String add(@AuthenticationPrincipal User user, @RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
            Message message = new Message(text, tag, user);
            messasgeRepository.save(message);
            Iterable<Message> messages = messasgeRepository.findAll();
            model.put("messages", messages);
            return "main";

        }



    }
