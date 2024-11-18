package ru.practicum.error.exception;

/**
 * Исключение, возникающее при нарушении валидации состояния.
 * Это исключение выбрасывается, когда состояние объекта или ресурса не соответствует ожидаемому.
 */
public class StateValidateException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public StateValidateException(String message) {
        super(message);
    }
}
