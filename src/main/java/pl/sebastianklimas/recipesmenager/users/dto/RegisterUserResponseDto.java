package pl.sebastianklimas.recipesmenager.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserResponseDto {
    @Schema(name = "User ID", example = "1")
    private Long id;
    @Schema(name = "User login", example = "user1")
    private String login;
    @Schema(name = "User email", example = "user@example.com")
    private String email;
}
