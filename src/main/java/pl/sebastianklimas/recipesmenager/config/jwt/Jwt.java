package pl.sebastianklimas.recipesmenager.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pl.sebastianklimas.recipesmenager.users.Role;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public List<Role> getRoles() {
        String[] rolesArray = claims.get("roles", String[].class);

        return Arrays.stream(rolesArray)
                .map(role -> Role.valueOf(role.toUpperCase()))
                .toList();
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
