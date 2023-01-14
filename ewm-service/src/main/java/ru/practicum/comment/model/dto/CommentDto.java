package ru.practicum.comment.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class CommentDto {
    String authorName;
    String text;
    @Builder.Default
    LocalDateTime created = LocalDateTime.now();
}
