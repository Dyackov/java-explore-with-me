package ru.practicum.error.exception;

public class StateValidateException extends RuntimeException {
    public StateValidateException(String message) {
        super(message);
    }
}