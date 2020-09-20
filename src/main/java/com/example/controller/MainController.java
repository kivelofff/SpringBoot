package com.example.controller;

import com.example.domain.Message;
import com.example.repos.MessasgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
        public String main(Map<String, Object> model) {
            Iterable<Message> messages = messasgeRepository.findAll();

            model.put("messages", messages);
            return "main";
        }

        @PostMapping("/main")
        public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
            Message message = new Message(text, tag);
            messasgeRepository.save(message);
            Iterable<Message> messages = messasgeRepository.findAll();
            model.put("messages", messages);
            return "main";

        }

        @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
            Iterable<Message> messages;
            if (filter != null && !filter.isEmpty()) {
                messages = messasgeRepository.findByTag(filter);
            } else {
                messages = messasgeRepository.findAll();
            }
            model.put("messages", messages);
            return "main";
        }

    }
