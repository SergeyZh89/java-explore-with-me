package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;

    @Length(min = 20, max = 2000)
    String annotation;

    CategoryDto category;

    long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @Length(min = 20, max = 7000)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    UserShortDto initiator;

    @NotNull
    LocationDto location;

    @NotNull
    boolean paid;

    @Min(value = 0)
    @Builder.Default
    int participantLimit = 0;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn = LocalDateTime.now();

    @Builder.Default
    boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    EventState state = EventState.PENDING;

    @Length(min = 3, max = 120)
    String title;

    @NotNull
    long views;

    List<CommentDto> comments = new ArrayList<>();
}