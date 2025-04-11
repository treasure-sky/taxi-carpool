package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// 토큰의 유효성 검사 및 SecurityContextHolder에 등록하는 커스텀 필터임. Config에 등록해서 사용하면 됨.
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
            String token = authHeader.substring(7); // "Bearer " 이후 토큰 부분임. 실제 엑세스 토큰.

            try {
                // 2) 토큰 검증
                if (jwtUtil.validateToken(token)) {
                    // 3) 토큰에서 사용자 식별값(email) 추출
                    String email = jwtUtil.getEmailFromToken(token);

                    // 4) 인증 객체 생성 (권한이 필요하면 loadUserByUsername() 등을 통해 가져올 수 있음)
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, null);
                    // 참고로 인수는 다음과 같다.
                    // Principal(사용자 식별 정보, 여기서는 email), Credentials(비밀번호 등 인증 수단, 여기서는 null), Authorities(권한 목록, 여기서는 null)

                    // 5) SecurityContextHolder에 등록(그래야 한 번의 request에서 필요한 사용자 정보를 공유하여 꺼내쓸 수 있음.)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (TokenExpiredException e) {
                // 토큰 만료 시 401 응답 -> 프론트측에서 이거 받고 Axios를 통해 재요청 보내면 댐.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Token 만료");
                return;
            } catch (TokenInvalidException e) {
                // 토큰이 위조되었거나 형식이 잘못됨
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("위조 or 형식이 잘못된 토큰");
                return;
            }
        }
        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
