package ru.practicum.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ValidatorException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.request.exception.RequestNotFountException;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RequestRepository requestRepository,
                           EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Request request = requestRepository.findRequestByRequester(userId)
                .orElseThrow(() -> new RequestNotFountException(requestId));

        request.setStatus(RequestState.CANCELED);

        return RequestMapper.INSTANCE.toRequestDto(requestRepository.save(request));
    }
}