package ru.practicum.service.impl;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.mappers.StatsMapper;
import ru.practicum.model.*;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.StatsService;
import ru.practicum.util.QPredicates;

import java.net.URLEncoder;
import java.util.ArrayList;
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
    public EndPointHit addHit(EndPointHitDto endPointHitDto) {
        EndPointHit endPointHit = StatsMapper.INSTANCE.toEndpointHit(endPointHitDto);
        return statsRepository.save(endPointHit);
    }

//    @Override
//    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
//        List<ViewStats> viewStatsList = statsRepository.findAll().stream()
//                .map(StatsMapper.INSTANCE::toViews).collect(Collectors.toList());
//
//        return viewStatsList;
//    }

    @Override
    public List<ViewStats> getStats(DtoRequestFilter filter) {
        QEndPointHit endPointHit = QEndPointHit.endPointHit;
        Predicate predicate = QPredicates.builder()
                .add(filter.getStart(), endPointHit.timeStamp::after)
                .add(filter.getEnd(), endPointHit.timeStamp::before)
                .add(filter.getUris(), endPointHit.uri::in)
                .buildAnd();
        Iterable<EndPointHit> endPointHitIterable = statsRepository.findAll(predicate);
        List<ViewStats> list = new ArrayList<>();
        for (EndPointHit pointHit : endPointHitIterable) {
            list.add(new ViewStats(pointHit.getApp(), pointHit.getUri(), statsRepository.countByApp(pointHit.getApp())));
        }
        if (filter.isUnique()) {
            return list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }
}
