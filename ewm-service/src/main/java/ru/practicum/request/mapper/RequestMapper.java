package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;

@Mapper
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestDto toRequestDto(Request request);
}
