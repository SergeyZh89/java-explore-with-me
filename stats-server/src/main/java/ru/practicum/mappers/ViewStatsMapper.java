package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

@Mapper
public interface ViewStatsMapper {
    ViewStatsMapper INSTANCE = Mappers.getMapper(ViewStatsMapper.class);

    ViewStats toViewStats(EndPointHit endPointHit);

    default ViewStats toViews(EndPointHit endPointHit) {
        return new ViewStats().toBuilder()
                .app(endPointHit.getApp())
                .uri(endPointHit.getUri())
                .build();
    }
}
