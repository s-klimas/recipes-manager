package pl.sebastianklimas.recipesmenager.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sebastianklimas.recipesmenager.users.dto.ChangePasswordRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;
import pl.sebastianklimas.recipesmenager.users.dto.UserDto;
import pl.sebastianklimas.recipesmenager.users.exceptions.DuplicateUserException;
import pl.sebastianklimas.recipesmenager.users.exceptions.UserNotFoundException;
import pl.sebastianklimas.recipesmenager.users.roles.RoleRepository;
import pl.sebastianklimas.recipesmenager.users.roles.RoleService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "john.doe@example.com";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String NEW_PASSWORD = "newPassword123";

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleService roleService;
    @Mock private RoleRepository roleRepository;
    @InjectMocks private UserService userService;

    private RegisterUserRequestDto requestDto;
    private User user;
    private UserRole userRole;
    private RegisterUserResponseDto responseDto;
    private UserDto userDto;
    private ChangePasswordRequestDto passwordRequest;

    @BeforeEach
    void setUp() {
        requestDto = new RegisterUserRequestDto();
        requestDto.setEmail(TEST_EMAIL);
        requestDto.setPassword(PASSWORD);
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(PASSWORD);
        userRole = new UserRole();
        responseDto = new RegisterUserResponseDto();
        responseDto.setEmail(TEST_EMAIL);
        userDto = new UserDto();
        userDto.setId(USER_ID);
        userDto.setEmail(USER_EMAIL);
        passwordRequest = new ChangePasswordRequestDto();
        passwordRequest.setOldPassword(PASSWORD);
        passwordRequest.setNewPassword(NEW_PASSWORD);
    }

    // ============================================================
    // Register User
    // ============================================================

    @Nested
    @DisplayName("Register User")
    class RegisterUserTests {

        @Test
        @DisplayName("Should throw DuplicateUserException when email already exists")
        void shouldThrowDuplicateUserException_WhenEmailAlreadyExists() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            assertThatThrownBy(() -> userService.registerUser(requestDto))
                    .isInstanceOf(DuplicateUserException.class)
                    .hasMessage("Email is already registered.");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUserSuccessfully() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(userMapper.toEntity(requestDto)).thenReturn(user);
            when(passwordEncoder.encode(PASSWORD)).thenReturn("encodedPassword");
            when(roleService.getRole(Role.USER.toString())).thenReturn(userRole);
            when(userMapper.toRegisterUserResponseDto(user)).thenReturn(responseDto);

            RegisterUserResponseDto result = userService.registerUser(requestDto);

            assertThat(result)
                    .isNotNull()
                    .extracting(RegisterUserResponseDto::getEmail)
                    .isEqualTo(TEST_EMAIL);

            verify(userRepository).save(user);
            verify(passwordEncoder).encode(PASSWORD);
        }
    }

    // ============================================================
    // Get User
    // ============================================================

    @Nested
    @DisplayName("Get User")
    class GetUserTests {

        @Test
        @DisplayName("Should return UserDto when user exists")
        void shouldReturnUserDto_WhenUserExists() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userMapper.toUserDto(user)).thenReturn(userDto);

            UserDto result = userService.getUser(USER_ID);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(userDto);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user not found")
        void shouldThrowException_WhenUserNotFound() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUser(999L))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User with id: 999 not found.");
        }
    }

    // ============================================================
    // Delete User
    // ============================================================

    @Nested
    @DisplayName("Delete User")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user when exists")
        void shouldDeleteUser_WhenUserExists() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

            userService.deleteUser(USER_ID);

            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("Should throw when user not found")
        void shouldThrowException_WhenUserDoesNotExist() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUser(999L))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User with id: 999 not found.");
        }

        @Test
        @DisplayName("Should propagate exception when repository delete fails")
        void shouldPropagateException_WhenRepositoryDeleteFails() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            doThrow(new RuntimeException("DB error")).when(userRepository).delete(user);

            assertThatThrownBy(() -> userService.deleteUser(USER_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DB error");
        }
    }

    // ============================================================
    // Change Password
    // ============================================================

    @Nested
    @DisplayName("Change Password")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password when old password is correct")
        void shouldChangePassword_WhenOldPasswordIsCorrect() {
            // given
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(PASSWORD, "password123")).thenReturn(true);
            when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn("encodedNewPassword");

            // when
            userService.changePassword(USER_ID, passwordRequest);

            // then
            verify(passwordEncoder).matches(PASSWORD, "password123");
            verify(passwordEncoder).encode(NEW_PASSWORD);
            verify(userRepository).save(user);
            assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
        }

        @Test
        @DisplayName("Should throw when old password is incorrect")
        void shouldThrowException_WhenOldPasswordIsIncorrect() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);

            assertThatThrownBy(() -> userService.changePassword(USER_ID, passwordRequest))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("Password does not match");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw when user not found during password change")
        void shouldThrowException_WhenUserNotFound_whileChangingPassword() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.changePassword(99L, passwordRequest))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User with id: 99 not found.");

            verify(passwordEncoder, never()).matches(anyString(), anyString());
        }
    }

    // ============================================================
    // Find User
    // ============================================================

    @Nested
    @DisplayName("Find User")
    class FindUserTests {

        @Test
        @DisplayName("Should return user when exists by id")
        void shouldReturnUser_WhenUserExistsById() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

            User result = userService.findUserById(USER_ID);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(user);
        }

        @Test
        @DisplayName("Should throw when user does not exist by id")
        void shouldThrowException_WhenUserDoesNotExistById() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findUserById(99L))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User with id: 99 not found.");
        }

        @Test
        @DisplayName("Should return user when exists by email")
        void shouldReturnUser_WhenUserExistsByEmail() {
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

            User result = userService.findUserByEmail(USER_EMAIL);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(user);
        }

        @Test
        @DisplayName("Should throw when user does not exist by email")
        void shouldThrowException_WhenUserDoesNotExistByEmail() {
            when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findUserByEmail("notfound@example.com"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User with email: notfound@example.com not found.");
        }
    }
}