package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.dto.EndPointHitDto;
import ru.practicum.service.StatsService;
import ru.practicum.service.impl.StatsServiceImpl;

import java.util.List;

@RestController
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service = new StatsServiceImpl();

    @GetMapping("/stats")
    public List<EndPointHit> getStats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false, defaultValue = "false") boolean unique) {
       return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndPointHitDto addHit(@RequestBody EndPointHitDto endPointHitDto) {
        return service.addHit(endPointHitDto);
    }
}
