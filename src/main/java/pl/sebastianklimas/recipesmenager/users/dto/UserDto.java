package pl.sebastianklimas.recipesmenager.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @Schema(name = "id", example = "1", description = "User ID")
    private Long id;
    @Schema(name = "login", example = "user1", description = "User login")
    private String login;
    @Schema(name = "email", example = "user@example.com", description = "User email")
    private String email;
    @Schema(name = "status", examples = {"ACTIVE", "INACTIVE", "SUSPENDED", "BANNED"}, description = "User status")
    private String status;
    @Schema(name = "roles", examples = {"USER", "ADMIN"}, description = "User roles")
    private List<String> roles;
}
