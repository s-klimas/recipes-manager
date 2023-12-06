package pl.sebastianklimas.recipesmenager.domain.passwordRecovery;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.domain.email.EmailService;
import pl.sebastianklimas.recipesmenager.domain.user.User;
import pl.sebastianklimas.recipesmenager.domain.user.UserRepository;

@Service
public class PasswordRecoveryService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordTokenService passwordTokenService;

    public PasswordRecoveryService(UserRepository userRepository, EmailService emailService, PasswordTokenService passwordTokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordTokenService = passwordTokenService;
    }

    @Transactional
    public void forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
        PasswordToken token = passwordTokenService.createPasswordToken(email);
        emailService.sendEmail(user.getEmail(), "Przypomnienie hasła!", getEmailText(token.getToken(), email));
    }

    private String getEmailText(String token, String email) {
        return "Aby ustawć nowe hasło klinkij tutaj: www.###.pl/recovery?token=" + token + "&email= " + email +". Jeżeli nie prosiłeś/aś o zresetowanie hasła zignoruj ten email.";
    }
}
