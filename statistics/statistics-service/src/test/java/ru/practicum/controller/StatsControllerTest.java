package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsControllerTest {

    private final ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsServiceImpl;

    @Test
    void createHit_shouldReturnStatusCreated() throws Exception {
        DtoEndpointHit hit = DtoEndpointHit.builder()
                .app("test-app")
                .uri("/test-uri")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        String dtoEndpointHitJson = mapper.writeValueAsString(hit);

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoEndpointHitJson))
                .andExpect(status().isCreated());

        verify(statisticsServiceImpl, times(1)).createHit(any(DtoEndpointHit.class));
    }

    @Test
    void getStats_shouldReturnStatsList() throws Exception {
        DtoViewStats viewStats = DtoViewStats.builder()
                .app("test-app")
                .uri("/test-uri")
                .hits(10L)
                .build();

        when(statisticsServiceImpl.getStats(any(), any(), any(), anyBoolean()))
                .thenReturn(Collections.singletonList(viewStats));

        mockMvc.perform(get("/stats")
                        .param("start", "2023-01-01 12:00:00")
                        .param("end", "2023-01-02 12:00:00")
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].app").value("test-app"))
                .andExpect(jsonPath("$[0].uri").value("/test-uri"))
                .andExpect(jsonPath("$[0].hits").value(10));

        verify(statisticsServiceImpl, times(1))
                .getStats(any(LocalDateTime.class), any(LocalDateTime.class), any(), anyBoolean());
    }
}
