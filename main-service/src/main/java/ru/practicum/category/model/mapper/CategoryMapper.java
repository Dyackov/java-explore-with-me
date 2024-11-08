package ru.practicum.category.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

@Mapper
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryDto categoryDto, @MappingTarget Category category);
}