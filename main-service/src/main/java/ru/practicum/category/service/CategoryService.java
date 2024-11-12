package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategoryAdmin(NewCategoryDto newCategoryDto);

    void deleteCategoryByIdAdmin(long id);

    CategoryDto updateCategoryByIdAdmin(long catId, NewCategoryDto newCategoryDto);

    CategoryDto getCategoryByIdPublic(long catId);

    List<CategoryDto> getCategoriesPublic(int from, int size);

    Category getCategoryByIdOrThrow(long catId);
}
