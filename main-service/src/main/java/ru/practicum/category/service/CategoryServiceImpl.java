package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.error.exception.IntegrityViolationException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.storage.EventRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategoryAdmin(NewCategoryDto newCategoryDto) {
        log.debug("Создание категории:\n{}", newCategoryDto);
        Category category = categoryMapper.toCategory(newCategoryDto);
        Category resultCategory = categoryRepository.save(category);
        log.info("Создана категория:\n{}", resultCategory);
        return categoryMapper.toCategoryDto(resultCategory);
    }

    @Override
    public void deleteCategoryByIdAdmin(long catId) {
        log.debug("Удаление категории id: {}", catId);
        getCategoryByIdOrThrow(catId);
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new IntegrityViolationException("Удаление категории невозможно. С категорией ID:" +
                    catId + " связаны события(е).");
        }
        categoryRepository.deleteById(catId);
        log.info("Категория с id: {} , удалёна", catId);
    }

    @Override
    public CategoryDto updateCategoryByIdAdmin(long catId, NewCategoryDto newCategoryDto) {
        log.debug("Обновление категории id: {}", catId);
        Category category = getCategoryByIdOrThrow(catId);
        category.setName(newCategoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Обновлена категория:\n{}", updatedCategory);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto getCategoryByIdPublic(long catId) {
        log.debug("Получение категории Id: {}", catId);
        Category category = getCategoryByIdOrThrow(catId);
        log.info("Получена категория:\n{}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategoriesPublic(int from, int size) {
        log.debug("Получение категорий.");
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        log.info("Получен список категорий:\n{}", categories);
        return categories.stream().map(categoryMapper::toCategoryDto).toList();
    }

    @Override
    public Category getCategoryByIdOrThrow(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException(
                "Category with id = " + catId + " was not found"));
    }

    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }
}
