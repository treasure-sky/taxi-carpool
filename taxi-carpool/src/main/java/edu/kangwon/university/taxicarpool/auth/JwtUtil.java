package edu.kangwon.university.taxicarpool.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 시크릿키는 시그니처를 만들 때, 서버만 알고 있는 시크릿 키를 사용하여
    // HMAC-SHA256(또는 다른 알고리즘) 방식으로 해시를 생성하는 용도
    // 시크릿키를 일단 간단하게 해놨는데, 실서비스 배포할 때는 .properties나 안전한 방법으로 관리로 바꿔야함.
    private final String SECRET_KEY = "12345678901234567890123456789012"; // 32바이트 이상
    private final long EXPIRATION = 1000L * 60 * 60; // 일단 1시간 유효로 해둠.

    // 토큰 생성
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        // 헤더랑 페이로드로 시그니처 만드는 과정임.
        return Jwts.builder()
            .setSubject(email)         // 토큰 식별자 (이메일 등)
            .setIssuedAt(now)          // 발급 시간
            .setExpiration(expiryDate) // 만료 시간
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSigningKey() {
        // 시그니처를 만들 때, 시크릿 키를 이용해서
        // HMAC-SHA256(알고리즘 이름임)방식으로 해시를 생성하는 함수임
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 토큰에서 email 추출
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // SignatureException, MalformedJwtException, ExpiredJwtException 등 자세한 예외처리는 나중에 추가 구현.
            return false;
        }
    }


}
