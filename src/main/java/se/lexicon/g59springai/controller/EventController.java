package se.lexicon.g59springai.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.g59springai.dto.Event;
import se.lexicon.g59springai.service.EventService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody @Valid Event event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addEvent(event));
    }

    @GetMapping
    public ResponseEntity<Collection<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable @NotBlank String id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable @NotBlank String id, @RequestBody @Valid Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable @NotBlank String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<Void> addParticipant(
            @PathVariable @NotBlank String id,
            @RequestParam @NotBlank @Email(message = "Invalid email format") String email) {
        eventService.addParticipant(id, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/participants")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable @NotBlank String id,
            @RequestParam @NotBlank @Email(message = "Invalid email format") String email) {
        eventService.removeParticipant(id, email);
        return ResponseEntity.noContent().build();
    }
}
