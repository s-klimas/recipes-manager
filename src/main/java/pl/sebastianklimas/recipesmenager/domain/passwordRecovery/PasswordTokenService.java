//package pl.sebastianklimas.recipesmenager.domain.passwordRecovery;
//
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.util.Calendar;
//
//@Service
//public class PasswordTokenService {
//    private final PasswordTokenRepository passwordTokenRepository;
//
//    public PasswordTokenService(PasswordTokenRepository passwordTokenRepository) {
//        this.passwordTokenRepository = passwordTokenRepository;
//    }
//
//    public PasswordToken createPasswordToken(String email) {
//        PasswordToken token = new PasswordToken();
//
//        Instant now = Instant.now();
//        Timestamp timestamp = Timestamp.from(now);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(timestamp);
//        cal.add(Calendar.DATE, 1);
//        Timestamp dayAfter = new Timestamp(cal.getTime().getTime());
//
//        token.setUserEmail(email);
//        token.setToken(RandomStringUtils.random(36, true, true));
//        token.setCreated(Timestamp.from(now));
//        token.setExpired(dayAfter);
//        token.setUsed(false);
//
//        passwordTokenRepository.save(token);
//        return token;
//    }
//}
