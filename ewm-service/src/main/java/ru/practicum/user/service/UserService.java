package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

    List<RequestDto> getUserRequests(long userId);

    RequestDto addNewRequestIntoEventByUser(long eventId, long userId);

    RequestDto cancelRequestByCurrentUser(long userId, long requestId);
}