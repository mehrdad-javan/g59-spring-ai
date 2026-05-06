package se.lexicon.g59springai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TravelParmeters(
        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "Month cannot be blank")
        @Pattern(
                regexp = "^(January|February|March|April|May|June|July|August|September|October|November|December)$",
                message = "Month must be a valid month name"
        )
        String month,

        @NotBlank(message = "Language cannot be blank")
        @Pattern(
                regexp = "^(English|Spanish|French|German|Italian|Persian|Arabic|Chinese|Japanese)$",
                message = "Language must be a supported language"
        )
        String language,

        @NotBlank(message = "Budget cannot be blank")
        @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Budget must be a valid decimal number")
        @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
        String budget
) {
}
