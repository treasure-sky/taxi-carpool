package edu.kangwon.university.taxicarpool.auth.authException;


// 엑세스 토큰 기한 만료 예외처리
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}