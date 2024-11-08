package ru.practicum.category.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoriesController {

    private final CategoryService categoryServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam int from,@RequestParam int size, HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на получение категорий. from: {}, size: {}", from,size);
        return categoryServiceImpl.getCategories(from,size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable int catId, HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на получение категории Id: {}", catId);
        return categoryServiceImpl.getCategoryById(catId);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
