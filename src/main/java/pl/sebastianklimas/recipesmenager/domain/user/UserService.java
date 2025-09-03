package pl.sebastianklimas.recipesmenager.domain.user;

import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordRecoveryDto;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordToken;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordTokenRepository;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenExpiredException;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenUsedException;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserCredentialsDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserIdentifyDto;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserRegistrationDto;

import javax.management.relation.RoleNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private static final String DEFAULT_USER_ROLE = "USER";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
//    private final PasswordTokenRepository passwordTokenRepository;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder){
//                       PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
//        this.passwordTokenRepository = passwordTokenRepository;
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

//    @Transactional
//    public void validateAndChangePassword(PasswordRecoveryDto prd) throws ChangeSetPersister.NotFoundException, TokenExpiredException, TokenUsedException {
//        PasswordToken token = passwordTokenRepository.findByToken(prd.getToken()).orElseThrow(ChangeSetPersister.NotFoundException::new);
//        if (validateToken(token)) {
//            token.setUsed(true);
//            passwordTokenRepository.save(token);
//            User user = userRepository.findByEmail(prd.getEmail()).orElseThrow(ChangeSetPersister.NotFoundException::new);
//            user.setPassword(passwordEncoder.encode(prd.getPassword()));
//            userRepository.save(user);
//        }
//    }
//
//    boolean validateToken(PasswordToken token) throws TokenUsedException, TokenExpiredException {
//        if (token.isUsed()) {
//            throw new TokenUsedException("Token is used");
//        }
//        Timestamp now = Timestamp.from(Instant.now());
//        Timestamp expired = token.getExpired();
//        if (now.after(expired)) {
//            throw new TokenExpiredException("Token is expired");
//        }
//        return true;
//    }
}
