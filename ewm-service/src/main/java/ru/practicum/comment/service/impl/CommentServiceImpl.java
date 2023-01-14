package ru.practicum.comment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.comment.exception.CommentNotFoundException;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentUpdate;
import ru.practicum.comment.model.dto.NewCommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ValidatorException;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    public CommentDto addNewComment(long userId, long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (user.isBanned() && user.getDateBan().isAfter(LocalDateTime.now())) {
            throw new ValidatorException(String.format("Пользователь забанен до %s и не может оставлять сообщения",
                    user.getDateBan().toString()));
        } else {
            user.setBanned(false);
            user.setDateBan(LocalDateTime.of(1000, 1, 1, 0, 0, 0));
        }
        Comment comment = new Comment().toBuilder()
                .author(user)
                .event(event)
                .authorName(user.getName())
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
        return CommentMapper.INSTANCE.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(long userId, long eventId, long commId) {
        checkUserOrThrowUserNotFound(userId);
        checkEventOrThrowEventNotFound(eventId);
        Comment commentFound = commentRepository
                .findById(commId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        if (commentFound.getAuthor().getId() != userId) {
            throw new ValidatorException("Нельзя удалять чужой комментарий");
        }
        if (commentFound.getCreated().isAfter(LocalDateTime.now().plusHours(24))) {
            throw new ValidatorException("Нельзя удалять комментарий после прошествия 24 часов");
        }
        commentRepository.deleteById(commId);
    }

    @Override
    public CommentDto updateComment(long userId, long eventId, CommentUpdate commentUpdate) {
        checkUserOrThrowUserNotFound(userId);
        checkEventOrThrowEventNotFound(eventId);
        Comment commentFound = commentRepository
                .findById(commentUpdate.getId())
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
        if (commentFound.getAuthor().getId() != userId) {
            throw new ValidatorException("Нельзя изменять чужой комментарий");
        }
        if (commentFound.getCreated().isAfter(LocalDateTime.now().plusHours(24))) {
            throw new ValidatorException("Нельзя удалять комментарий после прошествия 24 часов");
        }
        commentFound.setText(commentUpdate.getText());
        return CommentMapper.INSTANCE.toDto(commentRepository.save(commentFound));
    }

    @Override
    public List<CommentDto> getAllCommentsByUserID(long userId) {
        return commentRepository.findAllByAuthor_Id(userId).stream()
                .map(CommentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserComment(long userId, long commId) {
        if (!commentRepository.existsById(commId)) {
           throw new CommentNotFoundException("Комментарий не найден");
        }
        commentRepository.deleteById(commId);
    }

    @Override
    public List<CommentDto> searchCommentsByText(String text) {
        return commentRepository.findAllByTextContainingIgnoreCase(text).stream()
                .map(CommentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    private void checkUserOrThrowUserNotFound(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void checkEventOrThrowEventNotFound(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
    }
}