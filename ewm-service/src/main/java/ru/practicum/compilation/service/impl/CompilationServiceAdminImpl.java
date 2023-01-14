package ru.practicum.compilation.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(newCompilationDto);
        compilation.setEvents(eventList);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilationById(long complId) {
        if (!compilationRepository.existsById(complId)) {
            throw new CompilationException("Подборка не найдена");
        }
        compilationRepository.deleteById(complId);
    }

    @Override
    public void deleteEventFromCompilation(long complId, long eventId) {
        Compilation compilation = getCompilationOrThrowCompilationNotFound(complId);
        List<Event> eventList = compilation.getEvents().stream()
                .filter(event -> event.getId() != eventId)
                .collect(Collectors.toList());
        compilation.setEvents(eventList);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto addEventIntoCompilation(long complId, long eventId) {
        Compilation compilation = getCompilationOrThrowCompilationNotFound(complId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().add(event);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto unpinCompilationFromMainPage(long complId) {
        Compilation compilation = getCompilationOrThrowCompilationNotFound(complId);
        compilation.setPinned(false);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto pinCompilationOnMainPage(long complId) {
        Compilation compilation = getCompilationOrThrowCompilationNotFound(complId);
        compilation.setPinned(true);
        return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
    }

    private Compilation getCompilationOrThrowCompilationNotFound(long complId) {
        return compilationRepository.findById(complId)
                .orElseThrow(() -> new CompilationException("Подборка не найдена"));
    }
}