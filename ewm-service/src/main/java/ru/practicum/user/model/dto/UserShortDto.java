package ru.practicum.user.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserShortDto {
    @NotNull
    @Positive
    private long id;

    @NotNull
    private String name;
}