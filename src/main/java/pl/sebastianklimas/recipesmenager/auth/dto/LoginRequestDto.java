package pl.sebastianklimas.recipesmenager.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Email is required")
    @Email
    @Schema(name = "User's email", example = "user@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(name = "User's password", example = "user_password")
    private String password;
}
