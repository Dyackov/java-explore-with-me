package ru.practicum.event.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.enums.RequestState;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds = new HashSet<>();
    @NotNull
    private RequestState status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "\neventIds=" + requestIds +
                "\nstatus=" + status +
                '}';
    }
}
