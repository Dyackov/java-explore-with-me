package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategoryById(long id);

    CategoryDto updateCategoryById(long catId, NewCategoryDto newCategoryDto);

    CategoryDto getCategoryById(long catId);

    List<CategoryDto> getCategories(int from, int size);

    Category getCategoryByIdOrThrow(long catId);
}
