package ru.practicum.comment.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentUpdate {
    private Long id;
    @NotNull
    private String text;
}
