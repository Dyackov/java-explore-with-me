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
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto, HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на добавление новой категории:\n{}", newCategoryDto);
        return categoryServiceImpl.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable long catId, HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на удаление категории id: {}", catId);
        categoryServiceImpl.deleteCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategoryById(@PathVariable long catId,
                                          @RequestBody @Valid NewCategoryDto newCategoryDto,
                                          HttpServletRequest request) {
        logRequestDetails(request);
        return categoryServiceImpl.updateCategoryById(catId, newCategoryDto);
    }


    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }


}
