package ru.practicum.user.model.dto;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @ReadOnlyProperty
    @Positive
    private long id;

    private String name;

    @Email
    private String email;
}