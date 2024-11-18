package ru.practicum.category.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

/**
 * Маппер для преобразования объектов между сущностями {@link Category} и DTO {@link CategoryDto} / {@link NewCategoryDto}.
 * Используется для маппинга данных при передаче между слоями приложения.
 */
@Mapper
public interface CategoryMapper {

    /**
     * Преобразует объект {@link NewCategoryDto} в сущность {@link Category}.
     *
     * @param newCategoryDto DTO с данными для создания категории.
     * @return преобразованная сущность {@link Category}.
     */
    Category toCategory(NewCategoryDto newCategoryDto);

    /**
     * Преобразует сущность {@link Category} в объект {@link CategoryDto}.
     *
     * @param category сущность, которую нужно преобразовать.
     * @return DTO, представляющий категорию.
     */
    CategoryDto toCategoryDto(Category category);
}