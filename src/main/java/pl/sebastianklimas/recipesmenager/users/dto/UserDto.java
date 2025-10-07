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
    @Schema(name = "User ID", example = "1")
    private Long id;
    @Schema(name = "User login", example = "user1")
    private String login;
    @Schema(name = "User email", example = "user@example.com")
    private String email;
    @Schema(name = "User status", example = "ACTIVE")
    private String status;
    @Schema(name = "User roles", example = "USER, ADMIN")
    private List<String> roles;
}
