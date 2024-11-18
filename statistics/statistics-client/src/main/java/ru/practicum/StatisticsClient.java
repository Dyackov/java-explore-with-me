package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class StatisticsClient {


    private final RestClient restClient;

    public StatisticsClient(@Value("${app.statsUri}") String statsUri) {
        this.restClient = RestClient.builder()
                .baseUrl(statsUri)
                .build();
    }

    public void createHit(DtoEndpointHit dtoEndpointHit) {
        log.debug("Отправка POST-запроса на сервер статистики с hit = {}", dtoEndpointHit);
        try {
            restClient.post()
                    .uri("/hit")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dtoEndpointHit)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Hit отправлен успешно");
        } catch (RestClientException e) {
            log.warn("Ошибка при отправке hit на сервер статистики. Код ошибки: {}, Сообщение: {}",
                    e.getClass().getSimpleName(), e.getMessage());
        } catch (Exception e) {
            log.error("Неизвестная ошибка при отправке hit на сервер статистики", e);
        }
    }

    public List<DtoViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        log.info("Отправка GET-запроса на сервер статистики для uris = {}", uris);
        try {
            return restClient.get()
                    .uri(
                            uri -> uri.path("/stats")
                                    .queryParam("start", formatAndEncodeDateTime(start))
                                    .queryParam("end", formatAndEncodeDateTime(end))
                                    .queryParam("uris", uris)
                                    .queryParam("unique", unique)
                                    .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            log.warn("Ошибка при получении статистики с сервера статистики, причины : {}", e.getMessage());
            return List.of();
        } catch (Exception e) {
            log.error("Неизвестная ошибка при получении статистики", e);
            return List.of();
        }
    }

    private String formatAndEncodeDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}