package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.exception.NotFoundException;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.debug("Создание категории:\n{}", newCategoryDto);
        Category category = categoryMapper.toCategory(newCategoryDto);
        Category resultCategory = categoryRepository.save(category);
        log.info("Создана категория:\n{}", resultCategory);
        return categoryMapper.toCategoryDto(resultCategory);
    }

    //TODO: Обратите внимание: с категорией не должно быть связано ни одного события.
    @Override
    public void deleteCategoryById(long catId) {
        log.debug("Удаление категории id: {}", catId);
        getCategoryByIdOrThrow(catId);
        categoryRepository.deleteById(catId);
        log.info("Категория с id: {} , удалёна", catId);
    }

    @Override
    public CategoryDto updateCategoryById(long catId, NewCategoryDto newCategoryDto) {
        log.debug("Обновление категории id: {}", catId);
        Category category = getCategoryByIdOrThrow(catId);
        category.setName(newCategoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Обновлена категория:\n{}", updatedCategory);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public Category getCategoryByIdOrThrow(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException(
                "Category with id = " + catId + " was not found"));
    }
}
