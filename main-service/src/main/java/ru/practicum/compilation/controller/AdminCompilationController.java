package ru.practicum.compilation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

/**
 * Контроллер для администраторских операций с подборками.
 * Предоставляет функциональность для создания, удаления и обновления подборок.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationServiceImpl;

    /**
     * Создает новую подборку.
     *
     * @param newCompilationDto Данные для создания подборки
     * @param request           HTTP запрос
     * @return DTO новой подборки
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilationAdmin(@RequestBody @Valid NewCompilationDto newCompilationDto,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на добавление новой подборки.\n{}", newCompilationDto);
        return compilationServiceImpl.createCompilationAdmin(newCompilationDto);
    }

    /**
     * Удаляет подборку по ID.
     *
     * @param compId  ID подборки для удаления
     * @param request HTTP запрос
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationAdmin(@PathVariable long compId,
                                       HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на удаление подборки. ID подборки: {}", compId);
        compilationServiceImpl.deleteCompilationAdmin(compId);
    }

    /**
     * Обновляет данные подборки.
     *
     * @param compId                  ID подборки
     * @param updateCompilationRequest Данные для обновления подборки
     * @param request                 HTTP запрос
     * @return Обновленное DTO подборки
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilationAdmin(@PathVariable long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление подборки. ID подборки: {}\n{}", compId, updateCompilationRequest);
        return compilationServiceImpl.updateCompilationAdmin(compId, updateCompilationRequest);
    }

    /**
     * Логирует детали HTTP запроса, включая метод, URL и параметры.
     *
     * @param request HTTP запрос
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }

}