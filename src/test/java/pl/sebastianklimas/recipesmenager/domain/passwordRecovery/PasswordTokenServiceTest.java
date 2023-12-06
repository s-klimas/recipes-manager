package pl.sebastianklimas.recipesmenager.domain.passwordRecovery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordTokenServiceTest {
    @Mock
    private PasswordTokenRepository passwordTokenRepository;

    @InjectMocks
    private PasswordTokenService passwordTokenService;

    @BeforeEach
    public void setUp() {
        passwordTokenRepository = mock(PasswordTokenRepository.class);
        passwordTokenService = new PasswordTokenService(passwordTokenRepository);
    }

    @Test
    public void testCreatePasswordToken_ShouldSaveTokenWithCorrectData() {
        // given
        String userEmail = "test@example.com";

        // when


        // then
        PasswordToken resultToken = passwordTokenService.createPasswordToken(userEmail);
        verify(passwordTokenRepository, times(1)).save(any(PasswordToken.class));
        assertEquals(userEmail, resultToken.getUserEmail());
        assertNotNull(resultToken.getToken());
        assertFalse(resultToken.isUsed());

        Instant now = Instant.now();
        assertTrue(resultToken.getCreated().before(Timestamp.from(now)));
        assertTrue(resultToken.getExpired().after(Timestamp.from(now)));
    }

    @Test
    public void testCreatePasswordToken_ShouldGenerateUniqueTokens() {
        // given
        String userEmail = "test@example.com";

        // when

        // then
        PasswordToken token1 = passwordTokenService.createPasswordToken(userEmail);
        PasswordToken token2 = passwordTokenService.createPasswordToken(userEmail);
        assertNotEquals(token1.getToken(), token2.getToken());
    }
}