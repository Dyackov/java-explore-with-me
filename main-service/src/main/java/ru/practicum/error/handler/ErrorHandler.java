package ru.practicum.error.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.error.model.ApiError;
import ru.practicum.error.model.ApiErrors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
            NotFoundException.class})
    public ResponseEntity<ApiError> handle(final Exception e) {
        log.warn(e.getMessage());
        HttpStatus status;
        String reason;
        String message;

        switch (e) {
            case MethodArgumentNotValidException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Запрос составлен некорректно.";
                message = ex.getMessage();
            }
            case MissingServletRequestParameterException ex -> {
                message = String.format("Required parameter is missing: %s", ex.getParameterName());
                reason = "MissingServletRequestParameterException";
                status = HttpStatus.BAD_REQUEST;
            }
            case DataIntegrityViolationException ex -> {
                status = HttpStatus.CONFLICT;
                reason = "Ограничение целостности было нарушено.";
                message = ex.getMessage();
            }
            case NotFoundException ex -> {
                status = HttpStatus.NOT_FOUND;
                reason = "Искомый объект не был найден.";
                message = ex.getMessage();
            }
            default -> {
                message = e.getMessage();
                reason = "ValidationException";
                status = HttpStatus.BAD_REQUEST;
            }
        }

        ApiError apiError = ApiError.builder()
                .status(status.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleNotFound(final EntityNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        return ApiErrors.builder()
                .errors(errors)
                .message(e.getMessage())
                .reason("EntityNotFoundException")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}