package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;

@Mapper
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toCompilationDto(Compilation compilation);

    default Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation().toBuilder()
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}