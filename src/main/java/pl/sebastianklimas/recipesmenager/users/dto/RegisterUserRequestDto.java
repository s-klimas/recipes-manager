package pl.sebastianklimas.recipesmenager.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequestDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Schema(name = "login", example = "user1", maxLength = 255, description = "User login")
    private String login;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(name = "email", example = "user@example.com", description = "User email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(name = "password", example = "user_password", minLength =  6, maxLength = 25, description = "User password")
    private String password;
}
