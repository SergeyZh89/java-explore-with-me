package ru.practicum.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.AdminUpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventRequest;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    Event partialUpdate(UpdateEventRequest updateEventRequest, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    Event partialUpdate(AdminUpdateEventRequest adminUpdateEventRequest, @MappingTarget Event event);

    EventFullDto toFullEvent(Event event);

    @Mapping(target = "category", ignore = true)
    Event toEvent(NewEventDto newEventDto);
}