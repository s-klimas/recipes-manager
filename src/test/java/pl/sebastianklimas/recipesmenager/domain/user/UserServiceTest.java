//package pl.sebastianklimas.recipesmenager.domain.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.security.crypto.password.PasswordEncoder;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordRecoveryDto;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordRecoveryService;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordToken;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordTokenRepository;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenExpiredException;
////import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenUsedException;
//import pl.sebastianklimas.recipesmenager.domain.user.dto.UserCredentialsDto;
//import pl.sebastianklimas.recipesmenager.domain.user.dto.UserIdentifyDto;
//import pl.sebastianklimas.recipesmenager.domain.user.dto.UserRegistrationDto;
//
//import javax.management.relation.RoleNotFoundException;
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//    @Mock UserRepository userRepository;
//    @Mock UserRoleRepository userRoleRepository;
//    @Mock PasswordEncoder passwordEncoder;
//    @Mock
//    UserService userServiceMock;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    public void init() {
//        userRepository = mock(UserRepository.class);
//        userRoleRepository = mock(UserRoleRepository.class);
//        passwordEncoder = mock(PasswordEncoder.class);
//        userServiceMock = mock(UserService.class);
//        userService = new UserService(userRepository, userRoleRepository, passwordEncoder);
//    }
//
//    @Test
//    public void testing_FindCredentialsByEmail_WhenUserExists_ShouldReturnUserCredentialsDto() {
//        // given
//        String email = "test@example.com";
//        User testUser = new User();
//        testUser.setEmail(email);
//
//        // when
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
//        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(email);
//
//        // then
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    public void testing_FindCredentialsByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
//        // given
//        String email = "test@example.com";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        // when
//        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(email);
//
//        // then
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    public void testing_FindIdentifyDataByEmail_WhenUserExists_ShouldReturnUserIdentifyDto() {
//        // given
//        String email = "test@example.com";
//        User testUser = new User();
//        testUser.setEmail(email);
//
//        // when
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
//        Optional<UserIdentifyDto> result = userService.findIdentifyDataByEmail(email);
//
//        // then
//        assertThat(result).isPresent();
//    }
//
//    @Test
//    public void testing_FindIdentifyDataByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
//        // given
//        String email = "nonexistent@example.com";
//
//        // when
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//        Optional<UserIdentifyDto> result = userService.findIdentifyDataByEmail(email);
//
//        // then
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    public void testing_RegisterUserWithDefaultRole_WhenUserRegistrationIsValid_ShouldRegisterUser() throws RoleNotFoundException {
//        // given
//        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//        userRegistrationDto.setEmail("test@example.com");
//        userRegistrationDto.setPassword("password");
//        UserRole defaultRole = new UserRole();
//        defaultRole.setName("USER");
//
//        // when
//        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(defaultRole));
//        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("encoded_password");
//        userService.registerUserWithDefaultRole(userRegistrationDto);
//
//        // then
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void testing_RegisterUserWithDefaultRole_WhenDefaultRoleNotFound_ShouldThrowException() {
//        // given
//        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//        userRegistrationDto.setEmail("test@example.com");
//        userRegistrationDto.setPassword("password");
//
//        // when
//        when(userRoleRepository.findByName("USER")).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(RoleNotFoundException.class, () -> userService.registerUserWithDefaultRole(userRegistrationDto));
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @Test
//    public void testingRegisterUserWithDefaultRole_WhenUserRepositoryThrowsException_ShouldThrowException() {
//        // given
//        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//        userRegistrationDto.setEmail("test@example.com");
//        userRegistrationDto.setPassword("password");
//        UserRole defaultRole = new UserRole();
//        defaultRole.setName("USER");
//
//        // when
//        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(defaultRole));
//        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("encoded_password");
//        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
//
//        // then
//        assertThrows(RuntimeException.class, () -> userService.registerUserWithDefaultRole(userRegistrationDto));
//    }
//
////    @Test
////    public void testingValidateAndChange_WhenTokenIsValid_ShouldChangePassword() throws Exception {
////        // given
////        PasswordRecoveryDto passwordRecoveryDto = new PasswordRecoveryDto();
////        passwordRecoveryDto.setToken("validToken");
////        passwordRecoveryDto.setEmail("test@example.com");
////        passwordRecoveryDto.setPassword("newPassword");
////        PasswordToken mockToken = new PasswordToken();
////        mockToken.setUsed(false);
////        mockToken.setExpired(Timestamp.from(Instant.now().plusSeconds(3600)));
////        User mockUser = new User();
////        mockUser.setEmail(passwordRecoveryDto.getEmail());
////
////        // when
////        when(passwordTokenRepository.findByToken(passwordRecoveryDto.getToken())).thenReturn(Optional.of(mockToken));
////        when(userRepository.findByEmail(passwordRecoveryDto.getEmail())).thenReturn(Optional.of(mockUser));
////        when(userServiceMock.validateToken(mockToken)).thenReturn(true);
////
////        // then
////        userService.validateAndChangePassword(passwordRecoveryDto);
////        verify(passwordTokenRepository, times(1)).save(any(PasswordToken.class));
////        verify(userRepository, times(1)).save(any(User.class));
////    }
////
////    @Test
////    public void testingValidateAndChange_WhenTokenIsUsed_ShouldThrowTokenUsedException() {
////        // given
////        PasswordRecoveryDto passwordRecoveryDto = new PasswordRecoveryDto();
////        passwordRecoveryDto.setToken("usedToken");
////        passwordRecoveryDto.setEmail("test@example.com");
////        passwordRecoveryDto.setPassword("newPassword");
////        PasswordToken mockToken = new PasswordToken();
////        mockToken.setUsed(true);
////
////        // wwhen
////        when(passwordTokenRepository.findByToken(passwordRecoveryDto.getToken())).thenReturn(Optional.of(mockToken));
////
////        // then
////        assertThrows(TokenUsedException.class, () -> userService.validateAndChangePassword(passwordRecoveryDto));
////        verify(passwordTokenRepository, never()).save(any(PasswordToken.class));
////        verify(userRepository, never()).save(any(User.class));
////    }
////
////    @Test
////    public void testingValidateAndChange_WhenTokenIsExpired_ShouldThrowTokenExpiredException() {
////        // given
////        PasswordRecoveryDto passwordRecoveryDto = new PasswordRecoveryDto();
////        passwordRecoveryDto.setToken("expiredToken");
////        passwordRecoveryDto.setEmail("test@example.com");
////        passwordRecoveryDto.setPassword("newPassword");
////        PasswordToken mockToken = new PasswordToken();
////        mockToken.setUsed(false);
////        mockToken.setExpired(Timestamp.from(Instant.now().minusSeconds(3600)));
////
////        // when
////        when(passwordTokenRepository.findByToken(passwordRecoveryDto.getToken())).thenReturn(Optional.of(mockToken));
////
////        // then
////        assertThrows(TokenExpiredException.class, () -> userService.validateAndChangePassword(passwordRecoveryDto));
////        verify(passwordTokenRepository, never()).save(any(PasswordToken.class));
////        verify(userRepository, never()).save(any(User.class));
////    }
//}