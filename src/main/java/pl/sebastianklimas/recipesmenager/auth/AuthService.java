package pl.sebastianklimas.recipesmenager.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.auth.dto.LoginRequestDto;
import pl.sebastianklimas.recipesmenager.auth.dto.LoginResponseDto;
import pl.sebastianklimas.recipesmenager.config.jwt.Jwt;
import pl.sebastianklimas.recipesmenager.config.jwt.JwtService;
import pl.sebastianklimas.recipesmenager.users.User;
import pl.sebastianklimas.recipesmenager.users.UserService;

@AllArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userService.findUserByEmail(request.getEmail());
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var user = userService.findUserById(jwt.getUserId());
        return jwtService.generateAccessToken(user);
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return userService.findUserById(userId);
    }
}
