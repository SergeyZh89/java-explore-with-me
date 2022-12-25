package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndPointHitDto;

@Mapper
public interface StatsMapper {
    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    EndPointHit toEndpointHit(EndPointHitDto endPointHitDto);

    default ViewStats toViews(EndPointHit endPointHit) {
        return new ViewStats().toBuilder()
                .app(endPointHit.getApp())
                .uri(endPointHit.getUri())
                .build();
    }
}
