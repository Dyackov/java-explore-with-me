package ru.practicum.category.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

/**
 * Контроллер для получения категорий в публичной части приложения.
 * Обрабатывает запросы на получение всех категорий и категории по ID.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    /**
     * Сервис для работы с категориями.
     */
    private final CategoryService categoryServiceImpl;

    /**
     * Получение списка всех категорий с пагинацией.
     *
     * @param from начальная позиция для пагинации.
     * @param size количество элементов на странице.
     * @param request HTTP-запрос, используемый для логирования информации.
     * @return список объектов {@link CategoryDto}, представляющих категории.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategoriesPublic(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получен запрос на получение категорий. from: {}, size: {}", from, size);
        return categoryServiceImpl.getCategoriesPublic(from, size);
    }

    /**
     * Получение категории по ID.
     *
     * @param catId ID категории, которую нужно получить.
     * @param request HTTP-запрос, используемый для логирования информации.
     * @return объект {@link CategoryDto}, представляющий категорию.
     */
    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryByIdPublic(@PathVariable int catId,
                                             HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получен запрос на получение категории Id: {}", catId);
        return categoryServiceImpl.getCategoryByIdPublic(catId);
    }

    /**
     * Логирование деталей HTTP-запроса.
     *
     * @param request HTTP-запрос, содержащий информацию для логирования.
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}