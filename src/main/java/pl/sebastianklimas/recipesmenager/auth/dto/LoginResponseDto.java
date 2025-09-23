package pl.sebastianklimas.recipesmenager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sebastianklimas.recipesmenager.config.jwt.Jwt;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private Jwt accessToken;
    private Jwt refreshToken;
}
