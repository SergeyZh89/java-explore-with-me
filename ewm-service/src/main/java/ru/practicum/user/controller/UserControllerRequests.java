package ru.practicum.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.Request;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
public class UserControllerRequests {
    private final UserService userService;

    @Autowired
    public UserControllerRequests(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Request> getUserRequests(@PathVariable @Positive long userId) {
        log.info("Получен запрос о заявках текущего пользователя {}", userId);
        return userService.getUserRequests(userId);
    }

    @PostMapping
    public Request addNewRequestIntoEventByUser(@PathVariable @Positive long userId,
                                                @RequestParam(name = "eventId") @Positive long eventId) {
        log.info("Получен запрос о добавлении текущего пользователя {} на участие в событии {}", userId, eventId);
        return userService.addNewRequestIntoEventByUser(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public Request cancelRequestByCurrentUser(@PathVariable @Positive long userId,
                                              @PathVariable @Positive long requestId) {
        log.info("Получен запрос {} об отмене текущего пользователя {} на участие в событии", requestId, userId);
        return userService.cancelRequestByCurrentUser(userId, requestId);
    }
}