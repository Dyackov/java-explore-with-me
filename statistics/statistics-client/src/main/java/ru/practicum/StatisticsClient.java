package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.model.DtoEndpointHit;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StatisticsClient {
    private final RestTemplate restTemplate;

    public StatisticsClient(RestTemplateBuilder restTemplateBuilder, @Value("${app.statsUri}") String statsUri) {
        log.info("Client - Stats URI: {}", statsUri);
        this.restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsUri))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

    }

    public void createHit(DtoEndpointHit dtoEndpointHit) {
        HttpEntity<DtoEndpointHit> httpEntity = new HttpEntity<>(dtoEndpointHit);
        restTemplate.exchange("/hit", HttpMethod.POST, httpEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> params;
        String path;
        if (uris == null) {
            params = Map.of(
                    "start", formatAndEncodeDateTime(start),
                    "end", formatAndEncodeDateTime(end),
                    "unique", unique
            );
            path = "/stats?start={start}&end={end}&unique={unique}";
        } else {
            params = Map.of(
                    "start", formatAndEncodeDateTime(start),
                    "end", formatAndEncodeDateTime(end),
                    "uris", uris,
                    "unique", unique
            );
            path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        }
        return prepareGatewayResponse(restTemplate.getForEntity(path, Object.class, params));
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    private String formatAndEncodeDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        return URLEncoder.encode(formattedDate, StandardCharsets.UTF_8);
    }
}