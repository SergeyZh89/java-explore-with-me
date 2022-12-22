package ru.practicum.request.exception;

public class RequestNotFountException extends RuntimeException {
    public RequestNotFountException(long requestId) {
        super(String.format("Request with id=%d was not found", requestId));
    }
}