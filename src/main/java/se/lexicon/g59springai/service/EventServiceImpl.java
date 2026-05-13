package se.lexicon.g59springai.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import se.lexicon.g59springai.dto.Event;
import se.lexicon.g59springai.exception.DataNotFoundException;
import se.lexicon.g59springai.exception.DuplicateEntryException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final Map<String, Event> eventMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        String[] eventNames = {
                "Spring AI Workshop", "Java 21 Deep Dive", "Cloud Architecture Summit",
                "Frontend Trends 2026", "Data Science Bootcamp", "Cybersecurity Essentials",
                "Agile Leadership Forum", "Product Management Masterclass", "UI/UX Design Week",
                "Mobile App Development Expo", "Blockchain for Business", "AI Ethics Seminar",
                "DevOps Transformation Day", "Big Data Analytics Conference", "Open Source Contribution Day",
                "API Design Workshop", "Microservices Architecture Meetup", "FinTech Innovation Summit",
                "IoT Solutions Showcase", "Digital Marketing Strategy Session"
        };

        String[] locations = {
                "Stockholm", "Gothenburg", "Malmo", "Uppsala", "Vasteras",
                "Online", "New York", "London", "Berlin", "Paris",
                "Tokyo", "Sydney", "San Francisco", "Austin", "Dublin",
                "Amsterdam", "Helsinki", "Oslo", "Copenhagen", "Zurich"
        };

        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(10, 0));
        for (int i = 0; i < 20; i++) {
            String id = UUID.randomUUID().toString();
            // Create some past events and some future events
            // Today is 2026-03-23. Let's create events around this date.
            LocalDateTime dateTime = startDate.plusDays(i);
            Event event = new Event(
                    id,
                    eventNames[i],
                    "Explore the latest in " + eventNames[i].toLowerCase() + " with industry experts.",
                    dateTime,
                    locations[i],
                    new ArrayList<>()
            );
            eventMap.put(id, event);
        }
    }

    @Override
    @Tool(description = "Get all upcoming events")
    @Description("Returns a collection of all events that are scheduled for a future date and time")
    public Collection<Event> getAllEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventMap.values().stream()
                .filter(event -> event.dateTime().isAfter(now))
                .sorted(Comparator.comparing(Event::dateTime))
                .collect(Collectors.toList());
        System.out.println("events = " + events);
        return events;
    }

    @Override
    public Event getEventById(String id) {
        return Optional.ofNullable(eventMap.get(id))
                .orElseThrow(() -> new DataNotFoundException("Event not found with ID: " + id));
    }

    @Override
    @Tool(description = "Add a participant (book/register) to an event by event ID and participant email")
    @Description("Registers/books a participant for an event by adding their email to the event's participant list.")
    public void addParticipant(String eventId, String participantEmail) {
        validateEmail(participantEmail);
        Event event = getEventById(eventId);
        List<String> participants = event.participants();
        if (participants == null) {
            participants = new ArrayList<>();
            // Since Event is a record and immutable, we need to replace the event in the map
            // with a new instance that has a non-null participants list.
            event = new Event(event.id(), event.name(), event.description(), event.dateTime(), event.location(), participants);
            eventMap.put(eventId, event);
        }
        if (participants.contains(participantEmail)) {
            throw new DuplicateEntryException("Participant already added to the event: " + participantEmail);
        }
        participants.add(participantEmail);
    }

    @Override
    @Tool(description = "Remove a participant from an event by event ID and participant email")
    @Description("Unregisters a participant from an event by removing their email from the event's participant list.")
    public void removeParticipant(String eventId, String participantEmail) {
        validateEmail(participantEmail);
        Event event = getEventById(eventId);
        List<String> participants = event.participants();
        if (participants == null || !participants.remove(participantEmail)) {
            throw new DataNotFoundException("Participant not found in the event: " + participantEmail);
        }
    }

    @Override
    public Event addEvent(Event event) {
        String id = (event.id() == null || event.id().isEmpty()) ? UUID.randomUUID().toString() : event.id();
        if (eventMap.containsKey(id)) {
            throw new DuplicateEntryException("Event with ID " + id + " already exists.");
        }
        List<String> participants = event.participants() == null ? new ArrayList<>() : new ArrayList<>(event.participants());
        Event newEvent = new Event(id, event.name(), event.description(), event.dateTime(), event.location(), participants);
        eventMap.put(id, newEvent);
        return newEvent;
    }

    @Override
    public Event updateEvent(String id, Event event) {
        if (!eventMap.containsKey(id)) {
            throw new DataNotFoundException("Event not found with ID: " + id);
        }
        List<String> participants = event.participants() == null ? new ArrayList<>() : new ArrayList<>(event.participants());
        Event updatedEvent = new Event(id, event.name(), event.description(), event.dateTime(), event.location(), participants);
        eventMap.put(id, updatedEvent);
        return updatedEvent;
    }

    @Override
    public void deleteEvent(String id) {
        if (eventMap.remove(id) == null) {
            throw new DataNotFoundException("Event not found with ID: " + id);
        }
    }


    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

}
