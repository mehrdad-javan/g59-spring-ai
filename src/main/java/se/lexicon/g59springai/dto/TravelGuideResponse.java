package se.lexicon.g59springai.dto;

import java.util.List;

public record TravelGuideResponse(
        String destination,
        String month,
        List<String> topAttractions,
        List<String> localFood,
        String budgetSummary,
        String translationNote
) {
}
