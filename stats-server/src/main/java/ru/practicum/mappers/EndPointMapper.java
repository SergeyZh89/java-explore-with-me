package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.dto.EndPointHitDto;

@Mapper
public interface EndPointMapper {
    EndPointMapper INSTANCE = Mappers.getMapper(EndPointMapper.class);

    EndPointHitDto toEndPointHitDto(EndPointHit endPointHit);

    EndPointHit toEndPointHit(EndPointHitDto endPointHit);
}
