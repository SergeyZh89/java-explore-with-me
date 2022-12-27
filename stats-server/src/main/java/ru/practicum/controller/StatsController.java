package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mappers.DateTimeMapper;
import ru.practicum.model.DtoRequestFilter;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false, defaultValue = "new List<String>") List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Получен запрос просмотр статистики");
//        return statsService.getStats(start, end, uris, unique);
        DtoRequestFilter dtoRequestFilter =
                new DtoRequestFilter(DateTimeMapper.INSTANCE.toTime(start),
                        DateTimeMapper.INSTANCE.toTime(end), uris, unique);
        return statsService.getStats(dtoRequestFilter);
    }

    @PostMapping("/hit")
    public EndPointHit addHit(@RequestBody EndPointHitDto endPointHitDto) {
        log.info("Получен запрос на добавление EndPoint о просмотре события");
        return statsService.addHit(endPointHitDto);
    }
}