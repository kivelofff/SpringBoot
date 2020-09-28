package com.example.controller;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repos.MessasgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
        @Autowired
        private MessasgeRepository messasgeRepository;

        @Value("${upload.path}")
        private String uploadPath;
        @GetMapping("/")
        public String greeting() {

            return "greeting";
        }

        @GetMapping("/main")
        public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
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
        public String add(@AuthenticationPrincipal User user,
                          @RequestParam String text,
                          @RequestParam String tag, Map<String, Object> model,
                          @RequestParam("file")MultipartFile file) throws IOException {
            Message message = new Message(text, tag, user);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                message.setFilename(resultFilename);
            }
            messasgeRepository.save(message);
            Iterable<Message> messages = messasgeRepository.findAll();
            model.put("messages", messages);
            return "main";

        }



    }
