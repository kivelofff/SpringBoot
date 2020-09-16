package com.example;

import com.example.domain.Message;
import com.example.repos.MessasgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {
        @Autowired
        private MessasgeRepository messasgeRepository;

        @GetMapping("/greeting")
        public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Map<String, Object> model) {
            model.put("name", name);
            return "greeting";
        }

        @GetMapping
        public String main(Map<String, Object> model) {
            Iterable<Message> messages = messasgeRepository.findAll();

            model.put("messages", messages);
            return "main";
        }

    }
