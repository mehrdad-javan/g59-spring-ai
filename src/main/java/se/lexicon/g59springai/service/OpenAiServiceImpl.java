package se.lexicon.g59springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import se.lexicon.g59springai.dto.TravelGuideResponse;
import se.lexicon.g59springai.dto.TravelParmeters;

@Service
public class OpenAiServiceImpl implements OpenAIService {

    private final OpenAiChatModel openAiChatModel;

    private final ChatClient chatClient;

    @Autowired
    public OpenAiServiceImpl(OpenAiChatModel openAiChatModel, ChatClient.Builder builder) {
        this.openAiChatModel = openAiChatModel;
        this.chatClient = builder.build();
    }

    @Override
    public String processSimpleChatQuery(String query) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }

        return openAiChatModel.call(query);

    }


    @Override
    public String generateTravelGuide(TravelParmeters params) {
        if (params == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }

        // Define an AI role, tone, and behaviour
        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                        You are a professional travel assistant.
                        
                        Your role:
                        - Help users plan trips and explore destinations
                        - Provide clear, practical, and helpful travel advice
                        
                        Guidelines:
                        - Be friendly and concise
                        - Use bullet points or sections
                        - Suggest real-world recommendations
                        - If unsure, say: "I'm not sure about that"
                        """)
                .build();


        String userInput = String.format("""
                        Create a travel guide for:
                        
                        City: %s
                        Month: %s
                        Language: %s
                        Budget: %s
                        
                        Include:
                        1. Must-visit attractions
                        2. Local food recommendations
                        3. Useful phrases in the selected language
                        4. Budget travel tips
                        """,
                params.city(),
                params.month(),
                params.language(),
                params.budget());

        // Define tasks and goals
        UserMessage userMessage = UserMessage.builder()
                .text(userInput)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(
                        ChatOptions.builder()
                                .model("gpt-4o")
                                .temperature(0.4)
                                .maxTokens(1500)
                                .build())
                .build();

        ChatResponse response = openAiChatModel.call(prompt);
        String content = response.getResult() != null
                ? response.getResult().getOutput().getText()
                : null;
        return (content != null && !content.isBlank())
                ? content
                : "Sorry, I couldn't generate a response at the moment.";

    }

    @Override
    public String generateTravelGuideNew(TravelParmeters params) {
        if (params == null) {
            throw new IllegalArgumentException("TravelParameters cannot be null or empty");
        }
        ChatResponse response = chatClient.prompt()
                .system("""
                        You are a professional travel assistant.
                        
                        Your role:
                        - Help users plan trips and explore destinations
                        - Provide clear, practical, and helpful travel advice
                        
                        Guidelines:
                        - Be friendly and concise
                        - Use bullet points or sections
                        - Suggest real-world recommendations
                        - If unsure, say: "I'm not sure about that"
                        """)
                .user(String.format("""
                                Create a travel guide for:
                                
                                City: %s
                                Month: %s
                                Language: %s
                                Budget: %s
                                
                                Include:
                                1. Must-visit attractions
                                2. Local food recommendations
                                3. Useful phrases in the selected language
                                4. Budget travel tips
                                """,
                        params.city(),
                        params.month(),
                        params.language(),
                        params.budget()))
                .options(ChatOptions.builder()
                        .model("gpt-4o")
                        .maxTokens(1500)
                        .temperature(0.4))
                .call()
                .chatResponse();

        String content = response != null && response.getResult() != null
                ? response.getResult().getOutput().getText()
                : null;
        return (content != null && !content.isBlank())
                ? content
                : "Sorry, I couldn't generate a response at the moment.";

    }

    @Override
    public TravelGuideResponse generateTravelGuideJson(TravelParmeters params) {
        if (params == null) {
            throw new IllegalArgumentException("TravelParameters cannot be null");
        }
        BeanOutputConverter<TravelGuideResponse> converter = new BeanOutputConverter<>(TravelGuideResponse.class);

        String format = converter.getFormat();
        System.out.println("format = " + format);

        ChatResponse response = chatClient.prompt()
                .system("""
                        You are a professional travel assistant.
                        
                        Your role:
                        - Help users plan trips and explore destinations
                        - Provide clear, practical, and helpful travel advice
                        
                        Guidelines:
                        - Be friendly and concise
                        - Use bullet points or sections
                        - Suggest real-world recommendations
                        - If unsure, say: "I'm not sure about that"
                        
                        Format the output as a JSON object that matches this schema:
                        """ + format)
                .user(String.format("""
                                Create a travel guide for:
                                
                                City: %s
                                Month: %s
                                Language: %s
                                Budget: %s
                                
                                Include:
                                1. Must-visit attractions
                                2. Local food recommendations
                                3. Useful phrases in the selected language
                                4. Budget travel tips
                                """,
                        params.city(),
                        params.month(),
                        params.language(),
                        params.budget()))
                .options(ChatOptions.builder()
                        .model("gpt-4o")
                        .maxTokens(1500)
                        .temperature(0.4))
                .call()
                .chatResponse();

        String content = response != null && response.getResult() != null
                ? response.getResult().getOutput().getText()
                : null;

        if (content == null && content.isBlank()) {
            throw new IllegalArgumentException("No content found in the response");
        }

        return converter.convert(content);
    }

    @Override
    public Flux<String> processSimpleStreamChatQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        return openAiChatModel.stream(query);
    }
}
