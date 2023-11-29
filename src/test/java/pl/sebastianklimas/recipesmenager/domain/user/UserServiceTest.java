package pl.sebastianklimas.recipesmenager.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserCredentialsDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserIdentifyDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserRegistrationDto;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserRoleRepository userRoleRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, userRoleRepository, passwordEncoder);
    }

    @Test
    public void testing_FindCredentialsByEmail_WhenUserExists_ShouldReturnUserCredentialsDto() {
        // given
        String email = "test@example.com";
        User testUser = new User();
        testUser.setEmail(email);

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(email);

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void testing_FindCredentialsByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        // given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(email);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testing_FindIdentifyDataByEmail_WhenUserExists_ShouldReturnUserIdentifyDto() {
        // given
        String email = "test@example.com";
        User testUser = new User();
        testUser.setEmail(email);

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        Optional<UserIdentifyDto> result = userService.findIdentifyDataByEmail(email);

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void testing_FindIdentifyDataByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        // given
        String email = "nonexistent@example.com";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<UserIdentifyDto> result = userService.findIdentifyDataByEmail(email);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testing_RegisterUserWithDefaultRole_WhenUserRegistrationIsValid_ShouldRegisterUser() throws RoleNotFoundException {
        // given
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("password");
        UserRole defaultRole = new UserRole();
        defaultRole.setName("USER");

        // when
        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("encoded_password");
        userService.registerUserWithDefaultRole(userRegistrationDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testing_RegisterUserWithDefaultRole_WhenDefaultRoleNotFound_ShouldThrowException() {
        // given
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("password");

        // when
        when(userRoleRepository.findByName("USER")).thenReturn(Optional.empty());

        // then
        assertThrows(RoleNotFoundException.class, () -> userService.registerUserWithDefaultRole(userRegistrationDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUserWithDefaultRole_WhenUserRepositoryThrowsException_ShouldThrowException() {
        // given
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("password");
        UserRole defaultRole = new UserRole();
        defaultRole.setName("USER");

        // when
        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        // then
        assertThrows(RuntimeException.class, () -> userService.registerUserWithDefaultRole(userRegistrationDto));
    }
}