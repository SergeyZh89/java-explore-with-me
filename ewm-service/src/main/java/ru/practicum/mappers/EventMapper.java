package ru.practicum.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.NewEventDto;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    Event partialUpdate(EventDto eventDto, @MappingTarget Event event);

    default Event toEvent(NewEventDto newEventDto) {
        return new Event().toBuilder()
                .annotation(newEventDto.getAnnotation())
                .category(new Category(newEventDto.getCategory(), null))
                .description(newEventDto.getDescription())
                .location(newEventDto.getLocation())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }
}