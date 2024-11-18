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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilationAdmin(@RequestBody @Valid NewCompilationDto newCompilationDto,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на добавление новой подборки.\n{}", newCompilationDto);
        return compilationServiceImpl.createCompilationAdmin(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationAdmin(@PathVariable long compId,
                                       HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на удаление подборки. ID подборки: {}", compId);
        compilationServiceImpl.deleteCompilationAdmin(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilationAdmin(@PathVariable long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление подборки. ID подборки: {}\n{}", compId, updateCompilationRequest);
        return compilationServiceImpl.updateCompilationAdmin(compId, updateCompilationRequest);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }

}
