package pl.sebastianklimas.recipesmenager.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserResponseDto {
    private Long id;
    private String login;
    private String email;
}
