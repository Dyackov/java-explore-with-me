package ru.practicum.service;

import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    void createHit(DtoEndpointHit dtoEndpointHit);

    List<DtoViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}