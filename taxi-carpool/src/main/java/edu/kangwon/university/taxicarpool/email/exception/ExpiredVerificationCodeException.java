package edu.kangwon.university.taxicarpool.email.exception;

public class ExpiredVerificationCodeException extends RuntimeException {

    public ExpiredVerificationCodeException(String message) {
        super(message);
    }

}
