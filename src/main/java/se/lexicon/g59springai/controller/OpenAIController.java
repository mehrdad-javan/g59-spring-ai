package se.lexicon.g59springai.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.g59springai.service.OpenAIService;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Validated
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/chat")
    public String processSimpleChatQuery(@RequestParam @NotNull(message = "Question should not be null.") String question) {
        System.out.println("question = " + question);
        return openAIService.processSimpleChatQuery(question);
    }
}
