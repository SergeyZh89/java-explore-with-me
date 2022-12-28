package ru.practicum.service.impl;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.mappers.EndPointMapper;
import ru.practicum.mappers.ViewStatsMapper;
import ru.practicum.model.DtoRequestFilter;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.QEndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.StatsService;
import ru.practicum.util.QPredicates;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public EndPointHitDto addHit(EndPointHitDto endPointHitDto) {
        EndPointHit endPointHit = EndPointMapper.INSTANCE.toEndPointHit(endPointHitDto);
        return EndPointMapper.INSTANCE.toEndPointHitDto(statsRepository.save(endPointHit));
    }

    @Override
    public List<ViewStats> getStats(DtoRequestFilter filter) {
        QEndPointHit endPointHit = QEndPointHit.endPointHit;
        Predicate predicate = QPredicates.builder()
                .add(filter.getStart(), endPointHit.timeStamp::after)
                .add(filter.getEnd(), endPointHit.timeStamp::before)
                .add(filter.getUris(), endPointHit.uri::in)
                .buildAnd();

        List<ViewStats> test = StreamSupport.stream(statsRepository.findAll(predicate).spliterator(), false)
                .map(endPoint -> {
                    ViewStats viewStats = ViewStatsMapper.INSTANCE.toViewStats(endPoint);
                    viewStats.setHits(statsRepository.countByApp(endPoint.getApp()));
                    return viewStats;
                })
                .collect(Collectors.toList());

        if (filter.isUnique()) {
            return test.stream().distinct()
                    .collect(Collectors.toList());
        }
        return test;
    }
}