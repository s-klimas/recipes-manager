package pl.sebastianklimas.recipesmenager.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
class CustomSecurityConfig {
    private static final String USER_ROLE = "USER";
    private static final String EDITOR_ROLE = "EDITOR";
    private static final String ADMIN_ROLE = "ADMIN";
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                )
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}