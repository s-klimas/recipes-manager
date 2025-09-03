//package pl.sebastianklimas.recipesmenager.domain.passwordRecovery;
//
//import jakarta.persistence.*;
//
//import java.sql.Timestamp;
//
////@Entity
////@Table(name = "password_recovery_token")
//public class PasswordToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//    String userEmail;
//    String token;
//    Timestamp created;
//    Timestamp expired;
//    boolean used;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public Timestamp getCreated() {
//        return created;
//    }
//
//    public void setCreated(Timestamp created) {
//        this.created = created;
//    }
//
//    public Timestamp getExpired() {
//        return expired;
//    }
//
//    public void setExpired(Timestamp expired) {
//        this.expired = expired;
//    }
//
//    public boolean isUsed() {
//        return used;
//    }
//
//    public void setUsed(boolean used) {
//        this.used = used;
//    }
//}
