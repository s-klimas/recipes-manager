package pl.sebastianklimas.recipesmenager.users;

import org.mapstruct.Mapper;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserRequestDto registerUserRequestDto);
    RegisterUserResponseDto toDto(User user);
}
