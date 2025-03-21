package edu.kangwon.university.taxicarpool.auth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 암호화 알고리즘을 BCryptPasswordEncoder알고리즘 말고,
        // Pbkdf2PasswordEncoder로 해도 되는데, 추후에 의논해보기...
    }

}