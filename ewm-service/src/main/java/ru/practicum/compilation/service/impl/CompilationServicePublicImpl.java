package ru.practicum.compilation.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.exception.CompilationException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationServicePublic;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationRepository compilationRepository;

    public CompilationServicePublicImpl(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public List<Compilation> getCompilations(boolean pinned, Pageable pageable) {
        return compilationRepository.findAll(pageable).stream()
                .filter(compilation -> compilation.isPinned() == pinned)
                .collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationException("Такой подборки не существует"));
    }
}
