package ru.practicum.error.exception;

/**
 * Исключение, возникающее при ошибке валидации.
 * Это исключение выбрасывается, когда данные не проходят валидацию, например, при нарушении формата или других требований.
 */
public class ValidationException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public ValidationException(String message) {
        super(message);
    }
}