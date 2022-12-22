package ru.practicum.compilation.service;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.NewCompilationDto;

public interface CompilationServiceAdmin {
    Compilation addCompilation(NewCompilationDto compilationDto);

    void deleteCompilationById(long complId);

    void deleteEventFromCompilation(long complId, long eventId);

    Compilation addEventIntoCompilation(long complId, long eventId);

    Compilation unpinCompilationFromMainPage(long complId);

    Compilation pinCompilationOnMainPage(long complId);
}
