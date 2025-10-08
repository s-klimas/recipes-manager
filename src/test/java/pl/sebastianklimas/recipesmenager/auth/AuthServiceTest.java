package pl.sebastianklimas.recipesmenager.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sebastianklimas.recipesmenager.auth.dto.LoginRequestDto;
import pl.sebastianklimas.recipesmenager.config.jwt.Jwt;
import pl.sebastianklimas.recipesmenager.config.jwt.JwtService;
import pl.sebastianklimas.recipesmenager.users.User;
import pl.sebastianklimas.recipesmenager.users.UserService;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    // --- helpers ---
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor("super-secret-key-12345678901234567890".getBytes());
    }

    // ----------------------------------------------------------------------
    // login()
    // ----------------------------------------------------------------------
    @Test
    void shouldLoginAndReturnTokens() {
        // given
        var request = new LoginRequestDto("test@example.com", "password");
        var user = new User();

        Claims claimsAccess = Jwts.claims().build();
        Claims claimsRefresh = Jwts.claims().build();
        var accessToken = new Jwt(claimsAccess, secretKey);
        var refreshToken = new Jwt(claimsRefresh, secretKey);

        when(userService.findUserByEmail("test@example.com")).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        // when
        var result = authService.login(request);

        // then
        verify(authenticationManager).authenticate(any());
        verify(userService).findUserByEmail("test@example.com");

        assertThat(result.getAccessToken()).isSameAs(accessToken);
        assertThat(result.getRefreshToken()).isSameAs(refreshToken);
    }

    // ----------------------------------------------------------------------
    // refreshAccessToken()
    // ----------------------------------------------------------------------
    @Test
    void shouldThrowExceptionWhenRefreshTokenIsNull() {
        // given
        String refreshToken = "invalid-token";
        when(jwtService.parseToken(refreshToken)).thenReturn(null);

        // when / then
        assertThatThrownBy(() -> authService.refreshAccessToken(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenExpired() {
        // given
        Claims claims = Jwts.claims()
                .subject("123")
                .expiration(new Date(System.currentTimeMillis() - 1000)).build();

        Jwt expiredJwt = new Jwt(claims, secretKey);
        when(jwtService.parseToken("expired")).thenReturn(expiredJwt);

        // when / then
        assertThatThrownBy(() -> authService.refreshAccessToken("expired"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    void shouldRefreshAccessTokenSuccessfully() {
        // given
        Claims claims = Jwts.claims()
                .subject("42")
                .expiration(new Date(System.currentTimeMillis() + 100000))
                .build();

        Jwt validJwt = new Jwt(claims, secretKey);
        var user = new User();
        var newAccessToken = new Jwt(
                Jwts.claims().subject("42").build(),
                secretKey
        );

        when(jwtService.parseToken("refresh-token")).thenReturn(validJwt);
        when(userService.findUserById(42L)).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn(newAccessToken);

        // when
        var result = authService.refreshAccessToken("refresh-token");

        // then
        verify(jwtService).parseToken("refresh-token");
        verify(userService).findUserById(42L);
        verify(jwtService).generateAccessToken(user);

        assertThat(result).isSameAs(newAccessToken);
    }


    // ----------------------------------------------------------------------
    // getCurrentUser()
    // ----------------------------------------------------------------------
    @Test
    void shouldGetCurrentUserFromSecurityContext() {
        // given
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(101L);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        var user = new User();
        when(userService.findUserById(101L)).thenReturn(user);

        // when
        var result = authService.getCurrentUser();

        // then
        verify(userService).findUserById(101L);
        assertThat(result).isSameAs(user);
    }

    @AfterEach
    void cleanupSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
