package ru.practicum.user.model.dto;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @NotNull
    @Email
    private String email;

    @ReadOnlyProperty
    @PositiveOrZero
    private long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime date_ban;

    @NotNull
    private boolean isBanned;
}