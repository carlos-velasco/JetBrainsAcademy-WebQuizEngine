package engine.model.mapper;

import engine.model.dto.UserDto;
import engine.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "quizCompletions", ignore = true)
    @Mapping(target = "quizzes", ignore = true)
    User toUser(UserDto userDto);
}
