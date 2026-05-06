package se.lexicon.g59springai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import se.lexicon.g59springai.dto.TravelGuideResponse;
import se.lexicon.g59springai.dto.TravelParmeters;
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

    @PostMapping("/travel-guide")
    public String generateTravelGuide(@RequestBody @Valid TravelParmeters params){
        return openAIService.generateTravelGuide(params);
    }

    @PostMapping("/travel-guide/json")
    public TravelGuideResponse generateTravelGuideJson(@RequestBody @Valid TravelParmeters params){
        return openAIService.generateTravelGuideJson(params);
    }

    @GetMapping(value = "/chat/stream")
    public Flux<String> processSimpleStreamChatQuery(@RequestParam @NotNull(message = "Question should not be null.") String question) {
        System.out.println("question = " + question);
        return openAIService.processSimpleStreamChatQuery(question);
    }
}
