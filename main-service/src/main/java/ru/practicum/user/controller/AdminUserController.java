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

/**
 * Контроллер для управления пользователями администраторами.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userServiceImpl;

    /**
     * Создает нового пользователя.
     *
     * @param newUserRequest данные нового пользователя.
     * @param request        HTTP-запрос.
     * @return информация о созданном пользователе.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUserAdmin(@RequestBody @Valid NewUserRequest newUserRequest,
                                   HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на создание пользователя:\n{}", newUserRequest);
        return userServiceImpl.createUserAdmin(newUserRequest);
    }

    /**
     * Получает список пользователей.
     *
     * @param ids   список идентификаторов пользователей.
     * @param from  начальный индекс для пагинации.
     * @param size  количество пользователей для возврата.
     * @param request HTTP-запрос.
     * @return список пользователей.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersAdmin(@RequestParam(required = false) List<Integer> ids,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Получен запрос на получение информации о пользователях:\nid пользователей:{}, from:{}, size:{}",
                ids, from, size);
        return userServiceImpl.getUsersAdmin(ids, from, size);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param userId  идентификатор пользователя.
     * @param request HTTP-запрос.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByIdAdmin(@PathVariable long userId,
                                    HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на удаление пользователя id: {}", userId);
        userServiceImpl.deleteUserByIdAdmin(userId);
    }

    /**
     * Логирует детали HTTP-запроса.
     *
     * @param request HTTP-запрос.
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
