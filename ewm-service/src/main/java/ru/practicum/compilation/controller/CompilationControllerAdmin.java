package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
@Validated
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin compilationServiceAdmin;

    public CompilationControllerAdmin(CompilationServiceAdmin compilationServiceAdmin) {
        this.compilationServiceAdmin = compilationServiceAdmin;
    }

    @PostMapping
    public Compilation addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос на добавление новой подборки");
        return compilationServiceAdmin.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{complId}")
    public void deleteCompilation(@PathVariable @Positive long complId) {
        log.info("Получен запрос на удаление подборки {}", complId);
        compilationServiceAdmin.deleteCompilationById(complId);
    }

    @DeleteMapping("/{complId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable @Positive long eventId,
                                           @PathVariable @Positive long complId) {
        log.info("Получен запрос на удаление события {} из подборки {}", eventId, complId);
        compilationServiceAdmin.deleteEventFromCompilation(complId, eventId);
    }

    @PatchMapping("/{complId}/events/{eventId}")
    public Compilation addEventIntoCompilation(@PathVariable @Positive long complId,
                                               @PathVariable @Positive long eventId) {
        log.info("Получен запрос на добавление события {} в подборку {}", eventId, complId);
        return compilationServiceAdmin.addEventIntoCompilation(complId, eventId);
    }

    @DeleteMapping("/{complId}/pin")
    public Compilation unpinCompilationFromMainPage(@PathVariable @Positive long complId) {
        log.info("Получен запрос на открепление подборки с главной страницы");
        return compilationServiceAdmin.unpinCompilationFromMainPage(complId);
    }

    @PatchMapping("/{complId}/pin")
    public Compilation pinCompilationOnMainPage(@PathVariable @Positive long complId) {
        log.info("Получен запрос на закрепленик подборки на главной странице");
        return compilationServiceAdmin.pinCompilationOnMainPage(complId);
    }
}