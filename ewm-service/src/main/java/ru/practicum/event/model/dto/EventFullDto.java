package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.model.dto.UserShortDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private long id;

    @Length(min = 20, max = 2000)
    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    @NotNull
    private boolean paid;

    @Min(value = 0)
    @Builder.Default
    private int participantLimit = 0;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn = LocalDateTime.now();

    @Builder.Default
    private boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventState state = EventState.PENDING;

    @Length(min = 3, max = 120)
    private String title;

    @NotNull
    private long views;

    private List<CommentDto> comments = new ArrayList<>();
}