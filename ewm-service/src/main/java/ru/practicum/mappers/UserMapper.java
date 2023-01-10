package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(NewUserRequest newUserRequest);

    default UserDto toUserDto(User user) {
        return new UserDto().toBuilder()
                .name(user.getName())
                .id(user.getId())
                .isBanned(user.isBanned())
                .dateBan(user.getDateBan())
                .email(user.getEmail())
                .build();
    }
}