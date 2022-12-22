package ru.practicum.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(long eventId) {
        super(String.format("Event with id=%d was not found.", eventId));
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}