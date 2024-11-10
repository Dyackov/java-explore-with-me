package ru.practicum.request.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestForParticipationController {

    private final RequestService requestServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     HttpServletRequest request) {
        logRequestDetails(request);
        return requestServiceImpl.getRequests(userId);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto createRequest(@PathVariable long userId,
                                                 @RequestParam long eventId,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        return requestServiceImpl.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto updateRequest(@PathVariable long userId,
                                                 @PathVariable long requestId,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        return requestServiceImpl.updateRequest(userId, requestId);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
