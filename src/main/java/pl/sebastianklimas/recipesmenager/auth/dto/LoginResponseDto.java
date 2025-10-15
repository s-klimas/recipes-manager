package pl.sebastianklimas.recipesmenager.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sebastianklimas.recipesmenager.config.jwt.Jwt;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    @Schema(name = "accessToken", description = "Access token")
    private Jwt accessToken;
    @Schema(name = "refreshToken", description = "Refresh token")
    private Jwt refreshToken;
}
