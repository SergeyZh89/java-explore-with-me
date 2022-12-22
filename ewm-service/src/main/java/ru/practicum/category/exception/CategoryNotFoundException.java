package ru.practicum.category.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(long catId) {
        super(String.format("Category with id=%d was not found.", catId));
    }
}