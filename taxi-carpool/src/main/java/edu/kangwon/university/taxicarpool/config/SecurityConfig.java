package edu.kangwon.university.taxicarpool.config;

import static org.springframework.security.config.Customizer.withDefaults;

import edu.kangwon.university.taxicarpool.auth.JwtAuthenticationFilter;
import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 암호화 알고리즘을 BCryptPasswordEncoder알고리즘 말고,
        // Pbkdf2PasswordEncoder로 해도 되는데, 추후에 의논해보기...
    }

    // 모든 요청은 이 요청을 거쳐서 토큰의 유효성검사를 받아야함.(회원가입,로그인 요청은 제외해둠)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 커스텀 JWT 필터 생성
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil);

        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable()) // 필요에 따라 설정해야한다고 함.
            .authorizeHttpRequests(auth -> auth
                // 회원가입/로그인 엔드포인트는 인증 없이 접근 가능하게
                .requestMatchers(
                    "/",                        // 루트
                    "/chat/**",                 // websocket
                    "/api/auth/**",             // 회원가입, 로그인
                    "/api/email/**",            // 이메일 인증
                    "/api/password/reset-link",
                    "/api/password/reset",
                    "/swagger-ui.html",
                    "/swagger-ui/**",           // 스웨거 UI리소스
                    "/api-docs/**",          // 스웨거 API 문서
                    "/swagger-resources/**",    // 스웨거 리소스
                    "/webjars/**"              // 스웨거 관련 정적 리소스
                ).permitAll()

                .requestMatchers(HttpMethod.GET,
                    "/api/party/**",
                    "/api/map/search"
                ).permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            // 난 수동으로 회원가입 DB접근으로 검증해서 UsernamePasswordAuthenticationFilter 안 쓰이긴 함.
            // 나중에 .formLogin().disable()로 막아두는 것 고민하기.
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");           // 모든 출처 허용
        config.addAllowedMethod("*");                  // 모든 HTTP 메서드 허용
        config.addAllowedHeader("*");                  // 모든 헤더 허용
        config.setAllowCredentials(true);              // 쿠키/자격증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}