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

/**
 * Обработчик ошибок, перехватывающий различные исключения и возвращающий соответствующие ответы клиенту.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Обрабатывает различные типы исключений и возвращает детализированный ответ.
     *
     * @param e Исключение, которое произошло.
     * @return Ответ с подробной информацией об ошибке.
     */
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

        HttpStatus status;
        String reason;
        String message = e.getMessage();

        switch (e) {
            case ValidationException ex -> {
                status = HttpStatus.CONFLICT;
                reason = "Событие не удовлетворяет правилам редактирования.";
            }
            case MethodArgumentNotValidException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Запрос составлен некорректно.";
            }
            case MissingServletRequestParameterException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Требуемый параметр отсутствует.";
                message = String.format("Required parameter is missing: %s", ex.getParameterName());
            }
            case DataIntegrityViolationException ex -> {
                status = HttpStatus.CONFLICT;
                reason = "Ограничение целостности нарушено.";
            }
            case IntegrityViolationException ex -> {
                status = HttpStatus.CONFLICT;
                reason = "Ограничение целостности нарушено.";
            }
            case NotFoundException ex -> {
                status = HttpStatus.NOT_FOUND;
                reason = "Искомый объект не найден.";
            }
            case DataTimeException ex -> {
                status = HttpStatus.BAD_REQUEST;
                reason = "Неправильно составлен запрос с датой и временем";
            }
            case AuthorizationException ex -> {
                status = HttpStatus.UNAUTHORIZED;
                reason = "Не пройдена авторизация для запрошенной операции.";
            }
            case StateValidateException ex -> {
                status = HttpStatus.CONFLICT;
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

    /**
     * Обрабатывает исключение EntityNotFoundException.
     *
     * @param e Исключение, которое произошло.
     * @return Ответ с сообщением об ошибке.
     */
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