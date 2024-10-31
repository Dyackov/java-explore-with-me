package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaStatisticsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = """
            SELECT new ru.practicum.model.ViewStats(h.app, h.uri, count(distinct h.ip))
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN ?1 AND ?2
            GROUP BY h.app, h.uri, h.ip
            ORDER BY count(distinct h.ip) DESC
            """)
    List<ViewStats> findStatsAllUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT new ru.practicum.model.ViewStats(h.app, h.uri, count(h.ip))
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN ?1 AND ?2
            GROUP BY h.app, h.uri, h.ip
            ORDER BY count(h.ip) DESC
            """)
    List<ViewStats> findStatsAllNotUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT new ru.practicum.model.ViewStats(h.app, h.uri, count(distinct h.ip))
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3
            GROUP BY h.app, h.uri, h.ip
            ORDER BY count(distinct h.ip) DESC
            """)
    List<ViewStats> findUniqueIpByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = """
            SELECT new ru.practicum.model.ViewStats(h.app, h.uri, count(h.ip))
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3
            GROUP BY h.app, h.uri, h.ip
            ORDER BY count(h.ip) DESC
            """)
    List<ViewStats> findNotUniqueIpByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}