package ru.practicum.error.exception;

/**
 * Исключение, возникающее, когда запрашиваемый объект не найден.
 * Это исключение выбрасывается, когда попытка доступа к данным или ресурсу не удается, потому что они отсутствуют.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
