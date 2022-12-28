package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.user.exception.UserNotFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final EventNotFoundException e) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason("The required object was found.")
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final UserNotFoundException e, WebRequest request) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason("Пользователь не найден " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleValidationException(final ValidatorException e, WebRequest request) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(request.getDescription(false))
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(final CategoryNotFoundException e) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleThrowableExceptions(final Throwable e) {
        log.info("перехвачено исключение: " + e.getMessage());
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason("Throwable exception")
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}