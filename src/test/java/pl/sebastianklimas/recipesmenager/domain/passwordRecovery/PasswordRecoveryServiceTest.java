//package pl.sebastianklimas.recipesmenager.domain.passwordRecovery;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import pl.sebastianklimas.recipesmenager.domain.email.EmailService;
//import pl.sebastianklimas.recipesmenager.domain.user.User;
//import pl.sebastianklimas.recipesmenager.domain.user.UserRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class PasswordRecoveryServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private PasswordTokenService passwordTokenService;
//
//    @InjectMocks
//    private PasswordRecoveryService passwordRecoveryService;
//
//    @BeforeEach
//    public void setUp() {
//        userRepository = mock(UserRepository.class);
//        emailService = mock(EmailService.class);
//        passwordTokenService = mock(PasswordTokenService.class);
//        passwordRecoveryService = new PasswordRecoveryService(userRepository, emailService, passwordTokenService);
//    }
//
//    @Test
//    public void testingForgetPassword_WhenUserExists_ShouldCreateTokenAndSendEmail() {
//        // given
//        String userEmail = "test@example.com";
//        User mockUser = new User();
//        mockUser.setEmail(userEmail);
//        PasswordToken mockToken = new PasswordToken();
//        mockToken.setToken("testToken");
//
//        // when
//        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
//        when(passwordTokenService.createPasswordToken(userEmail)).thenReturn(mockToken);
//
//        // then
//        passwordRecoveryService.forgetPassword(userEmail);
//        verify(passwordTokenService, times(1)).createPasswordToken(userEmail);
//        verify(emailService, times(1)).sendEmail(eq(mockUser.getEmail()), eq("Przypomnienie hasła!"), anyString());
//    }
//
//    @Test
//    public void testingForgetPassword_WhenUserDoesNotExist_ShouldThrowException() {
//        // given
//        String userEmail = "nonexistent@example.com";
//
//        // when
//        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(UsernameNotFoundException.class, () -> passwordRecoveryService.forgetPassword(userEmail));
//        verify(passwordTokenService, never()).createPasswordToken(anyString());
//        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
//    }
//}