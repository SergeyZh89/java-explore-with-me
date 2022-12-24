package ru.practicum.event.enums;

import ru.practicum.exceptions.ValidatorException;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static String getSort(String sort) {
        switch (sort) {
            case "EVENT_DATE":
                return "eventDate";
            case "VIEWS":
                return "views";
            default:
                throw new ValidatorException("Нет такой сортировки");
        }
    }
}