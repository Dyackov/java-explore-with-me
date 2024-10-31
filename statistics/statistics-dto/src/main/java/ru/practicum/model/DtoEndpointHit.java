package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoEndpointHit {

    @NotNull(message = "Имя сервиса не может быть пустым.")
    private String app;

    @NotNull(message = "uri не может быть пустым.")
    private String uri;

    @NotNull(message = "ip не может быть пустым.")
    private String ip;

    @PastOrPresent(message = "Время не может быть в будущем.")
    @NotNull(message = "Время и дата не могут быть пустыми.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}