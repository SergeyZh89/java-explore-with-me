package ru.practicum.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/comments")
@Validated
public class CommentControllerAdmin {
    private final CommentService commentService;

    @Autowired
    public CommentControllerAdmin(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{userId}")
    public List<CommentDto> getAllCommentsByUserId(@PathVariable @Positive long userId) {
        log.info("Получен админский запрос на получение всех комментариев пользователя {}", userId);
        return commentService.getAllCommentsByUserID(userId);
    }

    @GetMapping
    public List<CommentDto> searchCommentsByText(@RequestParam String text) {
        log.info("Получен админский запрос на получение всех комментариев по тексту");
        return commentService.searchCommentsByText(text);
    }

    @DeleteMapping("{userId}")
    public void deleteCommentByIdAndByUserId(@PathVariable long userId, @RequestParam long commId) {
        log.info("Получен админский запрос на удаление комментария {} пользователя {}", commId, userId);
        commentService.deleteUserComment(userId, commId);
    }
}
