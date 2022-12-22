package ru.practicum.request;

import ru.practicum.event.enums.EventState;

public class ParticipationRequestDto {
    private long id;

    private long event;

    private long requester;

    private EventState status;
}
