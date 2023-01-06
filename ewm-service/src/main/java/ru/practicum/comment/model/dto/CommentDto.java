package ru.practicum.comment.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private String authorName;
    private String text;
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
