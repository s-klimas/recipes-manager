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
    @Schema(name = "email", example = "admin@example.com", description = "User's email")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(name = "password", example = "adminpass", description = "User's password")
    private String password;
}
