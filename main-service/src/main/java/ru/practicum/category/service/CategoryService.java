package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.error.exception.NotFoundException;

import java.util.List;

/**
 * Сервис для работы с категориями.
 * Предоставляет методы для создания, обновления, удаления и получения категорий,
 * а также для обработки запросов от администраторов и публичных пользователей.
 */
public interface CategoryService {

    /**
     * Создает новую категорию от имени администратора.
     *
     * @param newCategoryDto DTO, содержащий информацию о новой категории.
     * @return DTO созданной категории.
     */
    CategoryDto createCategoryAdmin(NewCategoryDto newCategoryDto);

    /**
     * Удаляет категорию по ее идентификатору от имени администратора.
     *
     * @param id идентификатор категории для удаления.
     */
    void deleteCategoryByIdAdmin(long id);

    /**
     * Обновляет информацию о категории от имени администратора.
     *
     * @param catId идентификатор категории для обновления.
     * @param newCategoryDto DTO, содержащий обновленную информацию о категории.
     * @return DTO обновленной категории.
     */
    CategoryDto updateCategoryByIdAdmin(long catId, NewCategoryDto newCategoryDto);

    /**
     * Получает информацию о категории по идентификатору для публичного доступа.
     *
     * @param catId идентификатор категории.
     * @return DTO категории.
     */
    CategoryDto getCategoryByIdPublic(long catId);

    /**
     * Получает список категорий с учетом пагинации для публичного доступа.
     *
     * @param from индекс первого элемента в списке.
     * @param size количество элементов, которые нужно вернуть.
     * @return список DTO категорий.
     */
    List<CategoryDto> getCategoriesPublic(int from, int size);

    /**
     * Получает категорию по идентификатору или выбрасывает исключение, если категория не найдена.
     *
     * @param catId идентификатор категории.
     * @return сущность {@link Category}.
     * @throws NotFoundException если категория с данным идентификатором не найдена.
     */
    Category getCategoryByIdOrThrow(long catId);
}