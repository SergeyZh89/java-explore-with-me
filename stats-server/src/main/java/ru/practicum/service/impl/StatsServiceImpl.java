package ru.practicum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.mappers.StatsMapper;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.StatsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public EndPointHit addHit(EndPointHitDto endPointHitDto) {
        EndPointHit endPointHit = StatsMapper.INSTANCE.toEndpointHit(endPointHitDto);
        return statsRepository.save(endPointHit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        List<ViewStats> viewStatsList = statsRepository.findAll().stream()
                .map(StatsMapper.INSTANCE::toViews).collect(Collectors.toList());
        return viewStatsList;
    }
}
