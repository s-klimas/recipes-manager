package pl.sebastianklimas.recipesmenager.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    @Schema(name = "User's old password", example = "user_password")
    private String oldPassword;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    @Schema(name = "User's new password", example = "new_user_password", minLength =  6, maxLength = 25)
    private String newPassword;
}
