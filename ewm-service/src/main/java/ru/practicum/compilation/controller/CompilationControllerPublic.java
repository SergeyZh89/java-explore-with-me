package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServicePublic;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
@Validated
public class CompilationControllerPublic {
    private final CompilationServicePublic compilationServicePublic;

    public CompilationControllerPublic(CompilationServicePublic compilationServicePublic) {
        this.compilationServicePublic = compilationServicePublic;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) boolean pinned,
                                                @RequestParam(name = "from", defaultValue = "0", required = false) int from,
                                                @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        log.info("Получен запрос на подборки событий");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return compilationServicePublic.getCompilations(pinned, pageable);
    }

    @GetMapping("/{complId}")
    public CompilationDto getCompilationsById(@PathVariable @Positive long complId) {
        log.info("Получен запрос на подборку событий {}", complId);
        return compilationServicePublic.getCompilationById(complId);
    }
}