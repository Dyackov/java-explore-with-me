package ru.practicum;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.DtoEndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class TestStatistics {
    private final StatisticsClient statisticsClient;

    public TestStatistics(StatisticsClient statisticsClient) {
        this.statisticsClient = statisticsClient;
    }

    @PostMapping("/hit")
    public ResponseEntity<String> createHit(@RequestBody @Valid DtoEndpointHit dtoEndpointHit) {
        log.info("Запрос на создание статистики:\n{}", dtoEndpointHit);
        statisticsClient.createHit(dtoEndpointHit);
        return ResponseEntity.status(HttpStatus.CREATED).body("Информация сохранена");
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
                Уникальные посещения: {}
                """, start, end, uris, unique);
        return statisticsClient.getStats(start, end, uris, unique);
    }
}