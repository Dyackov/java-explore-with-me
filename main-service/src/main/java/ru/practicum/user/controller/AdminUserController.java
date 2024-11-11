package ru.practicum.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest,
                              HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на создание пользователя:\n{}", newUserRequest);
        return userServiceImpl.createUser(newUserRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size,
                                  HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на получение информации о пользователях:\nid пользователей:{}, from:{}, size:{}",
                ids, from, size);
        return userServiceImpl.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable long userId,
                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на удаление пользователя id: {}", userId);
        userServiceImpl.deleteUserById(userId);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}