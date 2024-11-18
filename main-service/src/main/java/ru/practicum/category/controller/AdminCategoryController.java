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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategoryAdmin(@RequestBody @Valid NewCategoryDto newCategoryDto,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на добавление новой категории:\n{}", newCategoryDto);
        return categoryServiceImpl.createCategoryAdmin(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryByIdAdmin(@PathVariable long catId,
                                        HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на удаление категории id: {}", catId);
        categoryServiceImpl.deleteCategoryByIdAdmin(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategoryByIdAdmin(@PathVariable long catId,
                                               @RequestBody @Valid NewCategoryDto newCategoryDto,
                                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление категории id: {}", catId);
        return categoryServiceImpl.updateCategoryByIdAdmin(catId, newCategoryDto);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
