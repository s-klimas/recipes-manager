package pl.sebastianklimas.recipesmenager.users;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.users.dto.ChangePasswordRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;
import pl.sebastianklimas.recipesmenager.users.dto.UserDto;
import pl.sebastianklimas.recipesmenager.users.exceptions.DuplicateUserException;
import pl.sebastianklimas.recipesmenager.users.exceptions.UserNotFoundException;
import pl.sebastianklimas.recipesmenager.users.roles.RoleService;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public RegisterUserResponseDto registerUser(RegisterUserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateUserException("Email is already registered.");
        }

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(Status.ACTIVE.toString());

        UserRole role = roleService.getRole(Role.USER.toString());
        user.addRole(role);

        userRepository.save(user);

        return userMapper.toRegisterUserResponseDto(user);
    }

    public UserDto getUser(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found.")));
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found."));
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequestDto request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
