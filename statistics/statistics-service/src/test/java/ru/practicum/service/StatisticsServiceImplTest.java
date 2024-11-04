package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.DtoViewStats;
import ru.practicum.model.ViewStats;
import ru.practicum.storage.JpaStatisticsRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatisticsServiceImplTest {

    @Mock
    private JpaStatisticsRepository jpaStatisticsRepository;

    @Mock
    private ViewStatsMapper viewStatsMapper;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void getStats_whenEndBeforeStart_shouldThrowValidationException() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 2, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 1, 12, 0);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                statisticsService.getStats(start, end, Collections.emptyList(), false));

        assertThat(thrown.getMessage()).isEqualTo("Время конца не может быть раньше времени начала.");
    }

    @Test
    void getStats_whenUrisIsEmptyAndUniqueTrue_shouldCallFindStatsAllUniqueIp() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);

        List<ViewStats> viewStatsList = Arrays.asList(new ViewStats("app1", "/uri1", 10));
        when(jpaStatisticsRepository.findStatsAllUniqueIp(start, end)).thenReturn(viewStatsList);
        when(viewStatsMapper.toDtoViewStats(any(ViewStats.class))).thenAnswer(invocation -> {
            ViewStats viewStats = invocation.getArgument(0);
            return new DtoViewStats(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
        });

        List<DtoViewStats> result = statisticsService.getStats(start, end, Collections.emptyList(), true);

        assertThat(result).hasSize(1);
        verify(jpaStatisticsRepository, times(1)).findStatsAllUniqueIp(start, end);
    }

    @Test
    void getStats_whenUrisIsEmptyAndUniqueFalse_shouldCallFindStatsAllNotUniqueIp() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);

        List<ViewStats> viewStatsList = Arrays.asList(new ViewStats("app1", "/uri1", 10));
        when(jpaStatisticsRepository.findStatsAllNotUniqueIp(start, end)).thenReturn(viewStatsList);
        when(viewStatsMapper.toDtoViewStats(any(ViewStats.class))).thenAnswer(invocation -> {
            ViewStats viewStats = invocation.getArgument(0);
            return new DtoViewStats(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
        });

        List<DtoViewStats> result = statisticsService.getStats(start, end, Collections.emptyList(), false);

        assertThat(result).hasSize(1);
        verify(jpaStatisticsRepository, times(1)).findStatsAllNotUniqueIp(start, end);
    }

    @Test
    void getStats_whenUrisIsNotEmptyAndUniqueTrue_shouldCallFindUniqueIpByUris() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);
        List<String> uris = Arrays.asList("/uri1", "/uri2");

        List<ViewStats> viewStatsList = Arrays.asList(new ViewStats("app1", "/uri1", 10));
        when(jpaStatisticsRepository.findUniqueIpByUris(start, end, uris)).thenReturn(viewStatsList);
        when(viewStatsMapper.toDtoViewStats(any(ViewStats.class))).thenAnswer(invocation -> {
            ViewStats viewStats = invocation.getArgument(0);
            return new DtoViewStats(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
        });

        List<DtoViewStats> result = statisticsService.getStats(start, end, uris, true);

        assertThat(result).hasSize(1);
        verify(jpaStatisticsRepository, times(1)).findUniqueIpByUris(start, end, uris);
    }

    @Test
    void getStats_whenUrisIsNotEmptyAndUniqueFalse_shouldCallFindNotUniqueIpByUris() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);
        List<String> uris = Arrays.asList("/uri1", "/uri2");

        List<ViewStats> viewStatsList = Arrays.asList(new ViewStats("app1", "/uri1", 10));
        when(jpaStatisticsRepository.findNotUniqueIpByUris(start, end, uris)).thenReturn(viewStatsList);
        when(viewStatsMapper.toDtoViewStats(any(ViewStats.class))).thenAnswer(invocation -> {
            ViewStats viewStats = invocation.getArgument(0);
            return new DtoViewStats(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
        });

        List<DtoViewStats> result = statisticsService.getStats(start, end, uris, false);

        assertThat(result).hasSize(1);
        verify(jpaStatisticsRepository, times(1)).findNotUniqueIpByUris(start, end, uris);
    }
}