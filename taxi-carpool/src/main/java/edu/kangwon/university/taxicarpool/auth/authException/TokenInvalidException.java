package edu.kangwon.university.taxicarpool.auth.authException;


// 엑세스 유효성 예외처리(만료기한 제외)
public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}