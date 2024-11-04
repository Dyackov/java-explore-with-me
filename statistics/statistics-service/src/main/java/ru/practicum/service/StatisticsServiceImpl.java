package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.storage.JpaStatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final JpaStatisticsRepository jpaStatisticsRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public void createHit(DtoEndpointHit dtoEndpointHit) {
        EndpointHit endpointHit = endpointHitMapper.toStats(dtoEndpointHit);
        EndpointHit createEndpointHit = jpaStatisticsRepository.save(endpointHit);
        log.info("Создана статистика:\n{}", createEndpointHit);
    }

    @Override
    public List<DtoViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> viewStats;
        if (end.isBefore(start)) {
            throw new ValidationException("Время конца не может быть раньше времени начала.");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStats = jpaStatisticsRepository.findStatsAllUniqueIp(start, end);
            } else {
                viewStats = jpaStatisticsRepository.findStatsAllNotUniqueIp(start, end);
            }
        } else {
            if (unique) {
                viewStats = jpaStatisticsRepository.findUniqueIpByUris(start, end, uris);
            } else {
                viewStats = jpaStatisticsRepository.findNotUniqueIpByUris(start, end, uris);
            }
        }
        log.info("Получена статистика:\n{}", viewStats);
        return viewStats.stream()
                .map(viewStatsMapper::toDtoViewStats)
                .toList();
    }
}