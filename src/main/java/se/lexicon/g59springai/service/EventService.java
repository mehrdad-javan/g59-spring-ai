package se.lexicon.g59springai.service;

import se.lexicon.g59springai.dto.Event;

import java.util.Collection;

public interface EventService {
    Collection<Event> getAllEvents();

    Event getEventById(String id);

    void addParticipant(String eventId, String participantEmail);

    void removeParticipant(String eventId, String participantEmail);

    Event addEvent(Event event);

    Event updateEvent(String id, Event event);

    void deleteEvent(String id);
}
