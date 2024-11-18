package ru.practicum.error.exception;

public class IntegrityViolationException extends RuntimeException {
    public IntegrityViolationException(String message) {
        super(message);
    }
}
