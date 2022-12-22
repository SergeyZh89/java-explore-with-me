package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.location.Location;
import ru.practicum.user.model.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn = LocalDateTime.now();

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private long id;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private long participantLimit;

    @NotNull
    private String title;

    private long views;
}
