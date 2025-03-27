package com.sinensia.chatbot.controllers;

import com.sinensia.chatbot.services.OpenaiService;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenaiService openAiService;

    public ChatController(OpenaiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/message")
    public Map<String, String> sendMessage(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        String response = openAiService.enviarMensaje(prompt);
        return Collections.singletonMap("response", response);
    }
}
