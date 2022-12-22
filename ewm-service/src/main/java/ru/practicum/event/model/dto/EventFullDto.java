package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto {
    private long id;

    private String annotation;

    private long category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private long initiator;

    private long location;

    @Column(name = "paid")
    private boolean paid;

    @Min(value = 0)
    @Builder.Default
    @Column(name = "participantLimit")
    private int participantLimit = 0;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn = LocalDateTime.now();

    @Builder.Default
    private boolean requestModeration = true;

    private String state;

    private String title;

    private long views;
}
