package ru.practicum.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class UserControllerAdmin {
    private final UserService userService;

    @Autowired
    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids") List<Long> ids,
                                  @RequestParam(required = false, name = "from", defaultValue = "0") Integer from,
                                  @RequestParam(required = false, name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на список пользователей");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsers(ids, pageable);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid NewUserRequest userRequest) {
        log.info("Получен запрос на добавление пользователя {}", userRequest.getName());
        return userService.addUser(userRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос на удаление пользователя {}", userId);
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto setStatusUser(@PathVariable @Positive long userId,
                                 @RequestParam(name = "minutes", required = false, defaultValue = "0")
                                 @PositiveOrZero long minutes,
                                 @RequestParam(name = "ban") String isBanned) {
        log.info("Получен запрос на бан/разбан пользователя");
        return userService.setStatusUser(userId, minutes, isBanned);
    }
}