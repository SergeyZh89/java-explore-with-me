package ru.practicum.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super(String.format("User with id=%d was not found", userId));
    }
}