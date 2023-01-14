package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserControllerRequests {
    UserService userService;

    @GetMapping
    public List<RequestDto> getUserRequests(@PathVariable @Positive long userId) {
        log.info("Получен запрос о заявках текущего пользователя {}", userId);
        return userService.getUserRequests(userId);
    }

    @PostMapping
    public RequestDto addNewRequestIntoEventByUser(@PathVariable @Positive long userId,
                                                   @RequestParam(name = "eventId") @Positive long eventId) {
        log.info("Получен запрос о добавлении текущего пользователя {} на участие в событии {}", userId, eventId);
        return userService.addNewRequestIntoEventByUser(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequestByCurrentUser(@PathVariable @Positive long userId,
                                                 @PathVariable @Positive long requestId) {
        log.info("Получен запрос {} об отмене текущего пользователя {} на участие в событии", requestId, userId);
        return userService.cancelRequestByCurrentUser(userId, requestId);
    }
}