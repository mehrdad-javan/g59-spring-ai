package se.lexicon.g59springai.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.g59springai.service.EventChatbotAssistant;

@RestController
@RequestMapping("/api/v1/ai/event-chat")
@RequiredArgsConstructor
public class EventAiController {

    private final EventChatbotAssistant eventChatbotAssistant;

    @GetMapping
    public String chat(
            @RequestParam @NotBlank(message = "chatId cannot be blank") String chatId,
            @RequestParam @NotBlank(message = "message cannot be blank")String message){

        return eventChatbotAssistant.chat(chatId, message);
    }

}
