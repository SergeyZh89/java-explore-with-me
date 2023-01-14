package ru.practicum.user.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull
    @Email
    String email;

    @ReadOnlyProperty
    @PositiveOrZero
    long id;

    @NotNull
    String name;

    @NotNull
    LocalDateTime dateBan;

    @NotNull
    boolean isBanned;
}