package com.sinensia.chatbot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {

    @GetMapping("/chatbot")
    public String chatbotView() {
        return "chatbot"; // Se asume que el archivo es chatbot.html
    }
}
