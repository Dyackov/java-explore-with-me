package ru.practicum.event.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.enums.RequestState;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO класс для запроса на обновление статуса заявок на участие в событии.
 * Используется для передачи данных при обновлении статуса заявок на участие.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {

    /**
     * Множество идентификаторов заявок, для которых необходимо обновить статус.
     */
    private Set<Long> requestIds = new HashSet<>();

    /**
     * Новый статус, который должен быть установлен для заявок.
     * Не может быть пустым.
     */
    @NotNull
    private RequestState status;

    /**
     * Переопределение метода toString для удобного отображения информации о запросе на обновление статуса.
     *
     * @return Строковое представление объекта EventRequestStatusUpdateRequest.
     */
    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "\nrequestIds=" + requestIds +
                "\nstatus=" + status +
                '}';
    }
}
