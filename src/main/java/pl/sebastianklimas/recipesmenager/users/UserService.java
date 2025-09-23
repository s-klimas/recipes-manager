package pl.sebastianklimas.recipesmenager.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;
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
            throw new DuplicateUserException();
        }

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(Status.ACTIVE.toString());

        UserRole role = roleService.getRole(Role.USER.toString());
        user.addRole(role);

        userRepository.save(user);

        return userMapper.toDto(user);
    }
}
