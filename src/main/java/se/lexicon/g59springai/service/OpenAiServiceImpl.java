package se.lexicon.g59springai.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAiServiceImpl implements OpenAIService {

    private final OpenAiChatModel openAiChatModel;

    @Autowired
    public OpenAiServiceImpl(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    @Override
    public String processSimpleChatQuery(String query) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }
        try {
            String response = openAiChatModel.call(query);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
