package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.location.Location;
import ru.practicum.location.dto.LocationDto;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toLocation(LocationDto locationDto);
}
