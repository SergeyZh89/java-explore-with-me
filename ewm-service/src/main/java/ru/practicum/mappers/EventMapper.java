package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.NewEventDto;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

//    default Event toEvent(EventDto eventDto) {
//        return new Event().toBuilder()
//
//    }
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

//    Event toEvent(EventFullDto eventDto);
//
//    EventDto toDto(Event event);
//
//    EventDto toDto(EventShortDto eventDto);
//
//    EventDto toDto(EventFullDto eventDto);
}
