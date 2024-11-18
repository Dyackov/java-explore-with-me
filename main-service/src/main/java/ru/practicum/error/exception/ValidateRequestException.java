package ru.practicum.error.exception;

/**
 * Исключение, возникающее при ошибке валидации запроса.
 * Это исключение выбрасывается, когда данные, переданные в запросе, не соответствуют ожидаемым требованиям.
 */
public class ValidateRequestException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public ValidateRequestException(String message) {
        super(message);
    }
}
