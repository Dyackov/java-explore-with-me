package ru.practicum.compilation.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    private final CompilationService compilationServiceImpl;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationByIdPublic(@PathVariable long compId,
                                                   HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получен запрос на получение подборки. ID подборки: {}\n", compId);
        return compilationServiceImpl.getCompilationByIdPublic(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilationsPublic(@RequestParam(defaultValue = "false") Boolean pinned,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получен запрос на получение подборок. Закреп/Не закреп: {}, from: {}, size: {}",
                pinned, from, size);
        return compilationServiceImpl.getCompilationsPublic(pinned, from, size);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}