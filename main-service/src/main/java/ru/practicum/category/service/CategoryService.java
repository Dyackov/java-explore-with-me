package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategoryById(long id);

    CategoryDto updateCategoryById(long catId, NewCategoryDto newCategoryDto);

    Category getCategoryByIdOrThrow(long catId);
}
