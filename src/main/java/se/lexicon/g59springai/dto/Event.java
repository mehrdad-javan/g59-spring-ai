package se.lexicon.g59springai.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        String id,

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 255, message = "Description can be up to 255 characters")
        String description,

        @NotNull(message = "Date and time are required")
        @FutureOrPresent(message = "Date and time must be in the present or future")
        LocalDateTime dateTime,

        @NotBlank(message = "Location cannot be blank")
        String location,

        List<String> participants
) {
    public int getParticipantCount() {
        return participants == null ? 0 : participants.size();
    }
}
