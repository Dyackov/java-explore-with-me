package ru.practicum.category.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

/**
 * Контроллер для управления категориями в административной части приложения.
 * Обрабатывает запросы на создание, удаление и обновление категорий.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    /**
     * Сервис для работы с категориями.
     */
    private final CategoryService categoryServiceImpl;

    /**
     * Создание новой категории.
     *
     * @param newCategoryDto объект, содержащий данные для создания новой категории.
     * @param request HTTP-запрос, используемый для логирования информации.
     * @return объект {@link CategoryDto}, представляющий созданную категорию.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategoryAdmin(@RequestBody @Valid NewCategoryDto newCategoryDto,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на добавление новой категории:\n{}", newCategoryDto);
        return categoryServiceImpl.createCategoryAdmin(newCategoryDto);
    }

    /**
     * Удаление категории по ID.
     *
     * @param catId ID категории, которую необходимо удалить.
     * @param request HTTP-запрос, используемый для логирования информации.
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryByIdAdmin(@PathVariable long catId,
                                        HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на удаление категории id: {}", catId);
        categoryServiceImpl.deleteCategoryByIdAdmin(catId);
    }

    /**
     * Обновление категории по ID.
     *
     * @param catId ID категории, которую необходимо обновить.
     * @param newCategoryDto объект, содержащий новые данные для обновления категории.
     * @param request HTTP-запрос, используемый для логирования информации.
     * @return объект {@link CategoryDto}, представляющий обновленную категорию.
     */
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategoryByIdAdmin(@PathVariable long catId,
                                               @RequestBody @Valid NewCategoryDto newCategoryDto,
                                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление категории id: {}", catId);
        return categoryServiceImpl.updateCategoryByIdAdmin(catId, newCategoryDto);
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