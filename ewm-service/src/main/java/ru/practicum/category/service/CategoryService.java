package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories(Pageable pageable);

    Category getCategory(long catId);

    Category patchCategory(CategoryDto categoryDto);

    Category addCategory(NewCategoryDto categoryDto);

    void deleteCategory(long catId);
}