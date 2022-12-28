package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.user.model.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventShortDto {
    @NotNull
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private CategoryDto category;

    private long confirmedRequests;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private boolean paid;

    @NotNull
    @Length(min = 3, max = 120)
    private String title;

    private long views;
}