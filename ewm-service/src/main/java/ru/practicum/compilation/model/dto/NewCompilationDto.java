package ru.practicum.compilation.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCompilationDto {
    private Set<Long> events;

    private boolean pinned;

    @NotNull
    private String title;
}
