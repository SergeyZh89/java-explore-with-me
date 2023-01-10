package ru.practicum.comment.service;

import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentUpdate;
import ru.practicum.comment.model.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addNewComment(long userId, long eventId, NewCommentDto newCommentDto);

    void deleteComment(long userId, long eventId, long commId);

    void deleteUserComment(long userId, long commId);

    CommentDto updateComment(long userId, long eventId, CommentUpdate commentUpdate);

    List<CommentDto> getAllCommentsByUserID(long userId);

    List<CommentDto> searchCommentsByText(String text);
}
