package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotNull
    @Length(min = 20, max = 2000)
    String annotation;

    @NotNull
    @Positive
    long category;

    @NotNull
    @Length(min = 20, max = 7000)
    String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    Location location;

    @NotNull
    @Builder.Default
    boolean paid = false;

    @Builder.Default
    int participantLimit = 0;

    @NotNull
    @Builder.Default
    boolean requestModeration = true;

    @NotNull
    @Length(min = 3, max = 120)
    String title;
}