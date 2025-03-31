package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
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
    private final long ACCESS_EXPIRATION = 1000L * 60 * 60 * 2; // 일단 2시간 유효로 해둠.
    private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 일주일 유효
    // 토큰 검증 시(시그니처 만들 때) 사용할 시크릿 키(32바이트 이상)
    private final String SECRET_KEY = "12345678901234567890123456789012";

    // 엑세스 토큰 생성 메서드
    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_EXPIRATION);

        // 헤더랑 페이로드, 이메일(사용자 정보)로 시그니처 만듦.
        return Jwts.builder()
            .setSubject(email)         // 토큰 식별자 (이메일 등)
            .setIssuedAt(now)          // 발급 시간
            .setExpiration(expiryDate) // 만료 시간
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // 리프래쉬 토큰 생성 메서드 (만료 기간 1주)
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_EXPIRATION);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getSigningKey() {
        // 시그니처를 만들 때, 시크릿 키를 이용해서
        // HMAC-SHA256(알고리즘 이름임)방식으로 해시를 생성하는 함수임
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    // 토큰 검증 (추후에 통합으로 변경하기)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()// 이 빌더에서 내부적으로 만료 기한도 검사함.
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new TokenExpiredException("Access 토큰이 만료되었습니다.", e);
        } catch (SignatureException e) {
            // 시그니처 불일치 (위조 가능성)
            throw new TokenInvalidException("토큰 서명이 유효하지 않습니다.", e);
        } catch (MalformedJwtException e) {
            // 형식이 잘못된 JWT
            throw new TokenInvalidException("잘못된 토큰 형식입니다.", e);
        } catch (JwtException e) {
            // 그 외에 등등
            throw new TokenInvalidException("JWT 처리 중 오류가 발생했습니다.", e);
        }
    }


}
