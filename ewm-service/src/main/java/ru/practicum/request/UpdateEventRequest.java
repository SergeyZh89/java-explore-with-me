package ru.practicum.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class UpdateEventRequest {
    @Length(min = 20, max = 2000)
    private String annotation;

    private long category;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private long eventId;

    private boolean paid;

    private int participantLimit;

    @Length(min = 3, max = 120)
    private String title;
}