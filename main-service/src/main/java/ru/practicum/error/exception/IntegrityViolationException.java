package ru.practicum.error.exception;

/**
 * Исключение, возникающее при нарушении целостности данных.
 * Это исключение выбрасывается, когда операция нарушает ограничения целостности данных (например, при попытке вставить дублирующие данные или нарушить уникальные ключи).
 */
public class IntegrityViolationException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public IntegrityViolationException(String message) {
        super(message);
    }
}
