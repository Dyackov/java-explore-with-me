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
import ru.practicum.error.exception.*;
import ru.practicum.error.model.ApiError;
import ru.practicum.error.model.ApiErrors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({
            ValidationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
            NotFoundException.class,
            DataTimeException.class,
            AuthorizationException.class,
            StateValidateException.class,
            IntegrityViolationException.class,
            ValidateRequestException.class
    })
    public ResponseEntity<ApiError> handle(final Exception e) {
        log.warn(e.getMessage());

        HttpStatus status = HttpStatus.NO_CONTENT;
        String reason = "Некорректный запрос.";
        String message = e.getMessage();

        switch (e) {
            case MethodArgumentNotValidException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Запрос составлен некорректно.";
            }
            case MissingServletRequestParameterException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Требуемый параметр отсутствует.";
                message = String.format("Required parameter is missing: %s", ex.getParameterName());
            }
            case DataIntegrityViolationException exData -> {
                status = HttpStatus.CONFLICT;
                reason = "Ограничение целостности нарушено.";
            }
            case IntegrityViolationException exIntegrity -> {
                status = HttpStatus.CONFLICT;
                reason = "Ограничение целостности нарушено.";
            }
            case NotFoundException ex -> {
                status = HttpStatus.NOT_FOUND;
                reason = "Искомый объект не найден.";
            }
            case DataTimeException ex -> {
                if (ex.getMessage().contains("раньше, чем через два часа")) {
                    status = HttpStatus.BAD_REQUEST;
                    reason = "Неправильно составлен запрос с датой и временем";
                } else if (ex.getMessage().contains("не может быть в прошлом")) {
                    status = HttpStatus.FORBIDDEN;
                    reason = "Для запрошенной операции условия не выполнены.";
                }
            }
            case AuthorizationException ex -> {
                status = HttpStatus.UNAUTHORIZED;
                reason = "Не пройдена авторизация для запрошенной операции.";
            }
            case StateValidateException ex -> {
                status = HttpStatus.FORBIDDEN;
                reason = "Для запрошенной операции условия не выполнены.";
            }
            case ValidateRequestException ex -> {
                status = HttpStatus.CONFLICT;
                reason = "Запрос на участие в событии невозможен.";
            }
            default -> {
                reason = "Произошла ошибка при обработке запроса.";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
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