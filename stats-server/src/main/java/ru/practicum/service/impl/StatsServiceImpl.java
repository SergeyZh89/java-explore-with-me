package ru.practicum.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.service.StatsService;

import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    @Override
    public EndPointHitDto addHit(EndPointHitDto endPointHitDto) {
        return null;
    }

    @Override
    public List<EndPointHit> getStats(String start, String end, List<String> urie, boolean unique) {
        return null;
    }

    @Override
    public List<EndPointHit> getStats(String start, String end, boolean unique) {
        return null;
    }
}
