package pl.sebastianklimas.recipesmenager.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import pl.sebastianklimas.recipesmenager.auth.dto.LoginRequestDto;
import pl.sebastianklimas.recipesmenager.config.jwt.JwtConfig;
import pl.sebastianklimas.recipesmenager.config.jwt.JwtResponseDto;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    @Operation(summary = "Allows to log in.")
    public ResponseEntity<JwtResponseDto> login(
            @Parameter(description = "User's data to login.")
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse response
    ) {
        var loginResult = authService.login(request);

        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        var jwtResponse = new JwtResponseDto(loginResult.getAccessToken().toString());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Allows to get a new token from refresh token.")
    public JwtResponseDto refresh(
            @Parameter(description = "The refresh token.")
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponseDto(accessToken.toString());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
