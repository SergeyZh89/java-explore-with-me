package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationServicePublic {
    List<Compilation> getCompilations(boolean pinned, Pageable pageable);

    Compilation getCompilationById(long compId);
}
