package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.model.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Validated
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        log.info("Получен запрос на добавление новой категории {}", categoryDto.getName());
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получен запрос на обновление категории #{} name {}", categoryDto.getId(), categoryDto.getName());
        return categoryService.patchCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable @Positive long catId) {
        log.info("Получен запрос на удаление категории #{}", catId);
        categoryService.deleteCategory(catId);
    }
}