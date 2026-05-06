package se.lexicon.g59springai.service;

import se.lexicon.g59springai.dto.TravelGuideResponse;
import se.lexicon.g59springai.dto.TravelParmeters;

public interface OpenAIService {

    String processSimpleChatQuery(String query);

    String generateTravelGuide(TravelParmeters params);

    String generateTravelGuideNew(TravelParmeters params);

    TravelGuideResponse generateTravelGuideJson(TravelParmeters params);

}
