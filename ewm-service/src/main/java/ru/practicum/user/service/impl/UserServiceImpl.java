package ru.practicum.user.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ValidatorException;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.mappers.UserMapper;
import ru.practicum.request.exception.RequestNotFountException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RequestRepository requestRepository;
    EventRepository eventRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        return userRepository.findAllById(ids).stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        Optional<User> userFound = userRepository.findByEmail(newUserRequest.getEmail());
        if (userFound.isPresent()) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        User user = UserMapper.INSTANCE.toUser(newUserRequest);
        return UserMapper.INSTANCE.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long userId) {
        checkUserOrThrowUserNotFound(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<RequestDto> getUserRequests(long userId) {
        return requestRepository.findRequestsByRequester(userId).stream()
                .map(RequestMapper.INSTANCE::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto addNewRequestIntoEventByUser(long eventId, long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        checkUserOrThrowUserNotFound(userId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidatorException("Событие не опубликовано");
        } else if (event.getInitiator().getId() == userId) {
            throw new ValidatorException("Нельзя подавать запрос на свое событие");
        } else if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ValidatorException("Лимит запросов достигнут");
        }

        Request request;
        Optional<Request> requestFound = requestRepository.findRequestByRequester(userId);

        if (requestFound.isPresent()) {
            throw new ValidatorException("Реквест уже существует");
        } else {
            request = new Request();
        }

        request.setEvent(eventId);
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());
        if (!event.isRequestModeration()) {
            request.setStatus(RequestState.PENDING);
        }
        return RequestMapper.INSTANCE.toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelRequestByCurrentUser(long userId, long requestId) {
        checkUserOrThrowUserNotFound(userId);

        Request request = requestRepository.findRequestByRequester(userId)
                .orElseThrow(() -> new RequestNotFountException(requestId));

        request.setStatus(RequestState.CANCELED);

        return RequestMapper.INSTANCE.toRequestDto(requestRepository.save(request));
    }

    @Override
    public UserDto setStatusUser(long userId, long minutes, String isBanned) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (minutes == 0 && isBanned.equals("false")) {
            user.setDateBan(LocalDateTime.of(1000, 1, 1, 0, 0, 0));
            user.setBanned(false);
        } else if (minutes > 0 && isBanned.equals("true")) {
            user.setDateBan(LocalDateTime.now().plusMinutes(minutes));
            user.setBanned(true);
        } else if (minutes == 0 && isBanned.equals("true")) {
            user.setDateBan(LocalDateTime.of(3000, 1, 1, 0, 0, 0));
            user.setBanned(true);
        } else {
            throw new ValidatorException("Неверный запрос");
        }
        return UserMapper.INSTANCE.toUserDto(userRepository.save(user));
    }

    private void checkUserOrThrowUserNotFound(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }
}