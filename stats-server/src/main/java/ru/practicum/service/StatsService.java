package ru.practicum.service;

import ru.practicum.model.DtoRequestFilter;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndPointHitDto;

import java.util.List;

public interface StatsService {
    EndPointHitDto addHit(EndPointHitDto endPointHitDto);

    List<ViewStats> getStats(DtoRequestFilter dtoRequestFilter);
}