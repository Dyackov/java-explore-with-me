package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatisticsService statisticsServiceImpl;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody @Valid DtoEndpointHit dtoEndpointHit) {
        log.info("Запрос на создание статистики:\n{}", dtoEndpointHit);
        statisticsServiceImpl.createHit(dtoEndpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<DtoViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false", required = false)
                                       boolean unique) {
        log.info("""
                Запрос на получение статистики по посещениям:
                Диапазон даты и времени: {} - {}
                Список uri: {}
                Уникальные посещения: {}""", start, end, uris, unique);
        return statisticsServiceImpl.getStats(start, end, uris, unique);
    }
}