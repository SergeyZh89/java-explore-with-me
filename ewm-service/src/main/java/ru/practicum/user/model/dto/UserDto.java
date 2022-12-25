package ru.practicum.user.model.dto;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @ReadOnlyProperty
    @PositiveOrZero
    private long id;

    @NotNull
    private String name;

    @Email
    private String email;
}