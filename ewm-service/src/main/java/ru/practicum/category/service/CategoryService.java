package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(long catId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto addCategory(NewCategoryDto categoryDto);

    void deleteCategory(long catId);
}