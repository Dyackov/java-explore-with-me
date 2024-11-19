package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс запуска приложения.
 *
 * Этот класс инициализирует Spring Boot приложение и запускает его.
 */
@SpringBootApplication
public class MainServiceApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }
}