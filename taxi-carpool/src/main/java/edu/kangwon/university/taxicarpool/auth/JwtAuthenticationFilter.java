package edu.kangwon.university.taxicarpool.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// 토큰의 유효성을 검사하는 커스텀 필터임. Config에 등록해서 사용하면 됨.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        // 1) 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후 토큰 부분임. 실제 엑세스 토큰ㅇㅇ

            // 2) 토큰 검증
            if (jwtUtil.validateToken(token)) {
                // 3) 토큰에서 사용자 식별값(email) 추출
                String email = jwtUtil.getEmailFromToken(token);

                // 4) 인증 객체 생성 (권한이 필요하면 loadUserByUsername() 등을 통해 가져올 수 있음)
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);

                // 5) SecurityContextHolder에 등록(그래야 한 번의 request동안 계속 인증하지 않아도 댐)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
