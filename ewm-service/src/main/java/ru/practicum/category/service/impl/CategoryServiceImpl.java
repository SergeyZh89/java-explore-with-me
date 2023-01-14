package ru.practicum.category.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mappers.CategoryMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Optional<Category> categoryFound = categoryRepository.findByName(newCategoryDto.getName());
        if (categoryFound.isPresent()) {
            throw new ConflictException("Такая директория уже существует");
        }
        Category category = CategoryMapper.INSTANCE.toCategory(newCategoryDto);
        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper.INSTANCE::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository
                .findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId)));
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new CategoryNotFoundException(categoryDto.getId()));
        Optional<Category> categoryDuplicate = categoryRepository.findByName(categoryDto.getName());
        if (categoryDuplicate.isPresent()) {
            throw new ConflictException("Такая категория уже существует");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
        categoryRepository.deleteById(catId);
    }
}