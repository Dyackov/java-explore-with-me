package ru.practicum.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.error.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException exception) {
        log.warn(exception.getMessage(), exception);
        return ApiError.builder()
                .errors("Ошибка")
                .message(exception.getMessage())
                .reason("ValidationException")
                .status(HttpStatus.BAD_REQUEST.value() + "_" + HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}