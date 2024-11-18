package ru.practicum.error.exception;

/**
 * Исключение, возникающее при нарушении авторизации.
 * Это исключение выбрасывается, когда пользователь не имеет прав на выполнение запрашиваемой операции.
 */
public class AuthorizationException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public AuthorizationException(String message) {
        super(message);
    }
}
