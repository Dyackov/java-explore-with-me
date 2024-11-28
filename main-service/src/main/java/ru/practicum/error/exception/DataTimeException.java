package ru.practicum.error.exception;

/**
 * Исключение, возникающее при ошибке с датой и временем.
 * Это исключение выбрасывается, когда возникает некорректное использование или форматирование дат и времени.
 */
public class DataTimeException extends RuntimeException {

    /**
     * Конструктор для создания нового исключения с сообщением.
     *
     * @param message сообщение об ошибке, которое будет передано в исключение.
     */
    public DataTimeException(String message) {
        super(message);
    }
}
