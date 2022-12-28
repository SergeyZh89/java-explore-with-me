package ru.practicum.compilation.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.compilation.exception.CompilationException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationServiceAdmin;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.mappers.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceAdminImpl(CompilationRepository compilationRepository,
                                       EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(newCompilationDto);
        compilation.setEvents(eventList);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilationById(long complId) {
        Compilation compilation = compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
        compilationRepository.deleteById(complId);
    }

    @Override
    public void deleteEventFromCompilation(long complId, long eventId) {
        Compilation compilation = compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
        List<Event> eventList = compilation.getEvents().stream()
                .filter(event -> event.getId() != eventId)
                .collect(Collectors.toList());
        compilation.setEvents(eventList);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto addEventIntoCompilation(long complId, long eventId) {
        Compilation compilation = compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().add(event);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto unpinCompilationFromMainPage(long complId) {
        Compilation compilation = compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
        compilation.setPinned(false);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto pinCompilationOnMainPage(long complId) {
        Compilation compilation = compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
        compilation.setPinned(true);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }
}