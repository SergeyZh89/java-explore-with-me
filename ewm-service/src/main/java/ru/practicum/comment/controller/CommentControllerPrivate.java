package ru.practicum.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentUpdate;
import ru.practicum.comment.model.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class CommentControllerPrivate {
    CommentService commentService;

    @PostMapping
    public CommentDto addNewComment(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Получен запрос на добавление комментария от {} к событию {}", userId, eventId);
        return commentService.addNewComment(userId, eventId, newCommentDto);
    }

    @DeleteMapping("/{commId}")
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long eventId,
                              @PathVariable long commId) {
        log.info("Получен запрос на удаление комментария {} от пользователя {}", commId, userId);
        commentService.deleteComment(userId, eventId, commId);
    }

    @PatchMapping
    public CommentDto updateComment(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestBody @Valid CommentUpdate commentUpdate) {
        log.info("Получен запрос на изменение комментария {} от пользователя {}", commentUpdate.getId(), userId);
        return commentService.updateComment(userId, eventId, commentUpdate);
    }

    @GetMapping
    public List<CommentDto> searchCommentsByText(@RequestParam String text) {
        log.info("Получен запрос на получение всех комментариев по тексту");
        return commentService.searchCommentsByText(text);
    }
}