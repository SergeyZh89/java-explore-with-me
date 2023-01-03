package ru.practicum.compilation.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.exception.CompilationException;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationServicePublic;
import ru.practicum.mappers.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository compilationRepository;

    public CompilationServicePublicImpl(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, Pageable pageable) {
        return compilationRepository.findAll(pageable).stream()
                .filter(compilation -> compilation.isPinned() == pinned)
                .map(CompilationMapper.INSTANCE::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationException("Такой подборки не существует")));
    }
}