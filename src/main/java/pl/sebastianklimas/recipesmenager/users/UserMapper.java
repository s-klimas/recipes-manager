package pl.sebastianklimas.recipesmenager.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;
import pl.sebastianklimas.recipesmenager.users.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserRequestDto registerUserRequestDto);
    RegisterUserResponseDto toRegisterUserResponseDto(User user);
    @Mapping(target = "roles", expression = "java(user.getRolesNames())")
    UserDto toUserDto(User user);


}
