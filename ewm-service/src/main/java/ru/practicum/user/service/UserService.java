package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getUsers(List<Long> ids, Pageable pageable);

    User addUser(UserDto userDto);

    void deleteUser(long userId);

    List<Request> getUserRequests(long userId);

    Request addNewRequestIntoEventByUser(long eventId, long userId);

    Request cancelRequestByCurrentUser(long userId, long requestId);
}
