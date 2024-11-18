package ru.practicum.error.exception;

public class ValidateRequestException extends RuntimeException {
    public ValidateRequestException(String message) {
        super(message);
    }
}