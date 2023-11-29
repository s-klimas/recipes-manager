package pl.sebastianklimas.recipesmenager.domain.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserCredentialsDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserIdentifyDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserRegistrationDto;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    private static final String DEFAULT_USER_ROLE = "USER";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    public Optional<UserIdentifyDto> findIdentifyDataByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserIdentifyDtoMapper::map);
    }

    @Transactional
    public void registerUserWithDefaultRole(UserRegistrationDto userRegistration) throws RoleNotFoundException {
        UserRole defaultRole = userRoleRepository.findByName(DEFAULT_USER_ROLE).orElseThrow(RoleNotFoundException::new);
        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }
}
