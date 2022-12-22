package ru.practicum.service;

import ru.practicum.model.EndPointHit;
import ru.practicum.model.dto.EndPointHitDto;

import java.util.List;

public interface StatsService {
    EndPointHitDto addHit(EndPointHitDto endPointHitDto);

    List<EndPointHit> getStats(String start, String end, List<String> urie, boolean unique);

    List<EndPointHit> getStats(String start, String end, boolean unique);
}
