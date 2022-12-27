package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface DateTimeMapper {
    DateTimeMapper INSTANCE = Mappers.getMapper(DateTimeMapper.class);
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    default LocalDateTime toTime(String time) {
        return LocalDateTime.parse(time, FORMATTER);
    }
}
