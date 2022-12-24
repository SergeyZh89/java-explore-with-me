package ru.practicum.user.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserShortDto {
    private long id;

    private String name;
}