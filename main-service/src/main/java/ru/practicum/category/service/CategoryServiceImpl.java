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

/**
 * Реализация сервиса для работы с категориями.
 * Обрабатывает запросы на создание, обновление, удаление категорий,
 * а также получение категорий для публичных и административных интерфейсов.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Создает новую категорию от имени администратора.
     *
     * @param newCategoryDto DTO с данными новой категории.
     * @return DTO созданной категории.
     */
    @Override
    public CategoryDto createCategoryAdmin(NewCategoryDto newCategoryDto) {
        log.debug("Admin:Создание категории:\n{}", newCategoryDto);
        Category category = categoryMapper.toCategory(newCategoryDto);
        Category resultCategory = categoryRepository.save(category);
        log.info("Admin:Создана категория:\n{}", resultCategory);
        return categoryMapper.toCategoryDto(resultCategory);
    }

    /**
     * Удаляет категорию по идентификатору от имени администратора.
     * Проверяет, не связана ли категория с событиями.
     *
     * @param catId идентификатор категории.
     * @throws IntegrityViolationException если категория связана с событиями.
     */
    @Override
    public void deleteCategoryByIdAdmin(long catId) {
        log.debug("Admin:Удаление категории id: {}", catId);
        getCategoryByIdOrThrow(catId);
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new IntegrityViolationException("Удаление категории невозможно. С категорией ID:" +
                    catId + " связаны события(е).");
        }
        categoryRepository.deleteById(catId);
        log.info("Admin:Категория с id: {} , удалёна", catId);
    }

    /**
     * Обновляет категорию по идентификатору от имени администратора.
     *
     * @param catId идентификатор категории для обновления.
     * @param newCategoryDto DTO с обновленными данными категории.
     * @return DTO обновленной категории.
     */
    @Override
    public CategoryDto updateCategoryByIdAdmin(long catId, NewCategoryDto newCategoryDto) {
        log.debug("Admin:Обновление категории id: {}", catId);
        Category category = getCategoryByIdOrThrow(catId);
        category.setName(newCategoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Admin:Обновлена категория:\n{}", updatedCategory);
        return categoryMapper.toCategoryDto(category);
    }

    /**
     * Получает категорию по идентификатору для публичного доступа.
     *
     * @param catId идентификатор категории.
     * @return DTO категории.
     */
    @Override
    public CategoryDto getCategoryByIdPublic(long catId) {
        log.debug("Public:Получение категории Id: {}", catId);
        Category category = getCategoryByIdOrThrow(catId);
        log.info("Public:Получена категория:\n{}", category);
        return categoryMapper.toCategoryDto(category);
    }

    /**
     * Получает список категорий с пагинацией для публичного доступа.
     *
     * @param from индекс первого элемента в списке.
     * @param size количество элементов на странице.
     * @return список DTO категорий.
     */
    @Override
    public List<CategoryDto> getCategoriesPublic(int from, int size) {
        log.debug("Public:Получение категорий.");
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        log.info("Public:Получен список категорий:\n{}", categories);
        return categories.stream().map(categoryMapper::toCategoryDto).toList();
    }

    /**
     * Получает категорию по идентификатору или выбрасывает исключение, если категория не найдена.
     *
     * @param catId идентификатор категории.
     * @return сущность категории.
     * @throws NotFoundException если категория не найдена.
     */
    @Override
    public Category getCategoryByIdOrThrow(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException(
                "Category with id = " + catId + " was not found"));
    }

    /**
     * Создает объект Pageable для пагинации.
     *
     * @param from индекс первого элемента.
     * @param size размер страницы.
     * @param sort сортировка.
     * @return объект Pageable.
     */
    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }
}