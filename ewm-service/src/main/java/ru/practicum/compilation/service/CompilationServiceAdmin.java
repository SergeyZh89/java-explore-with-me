package ru.practicum.compilation.service;

import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;

public interface CompilationServiceAdmin {
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilationById(long complId);

    void deleteEventFromCompilation(long complId, long eventId);

    CompilationDto addEventIntoCompilation(long complId, long eventId);

    CompilationDto unpinCompilationFromMainPage(long complId);

    CompilationDto pinCompilationOnMainPage(long complId);
}