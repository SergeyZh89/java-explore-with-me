package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.model.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {
    List<CompilationDto> getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(long compId);
}