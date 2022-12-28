package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0", required = false) int from,
                                           @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getCategories(pageable);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive long catId) {
        return categoryService.getCategory(catId);
    }
}