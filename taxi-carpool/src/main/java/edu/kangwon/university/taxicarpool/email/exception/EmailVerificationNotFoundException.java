package edu.kangwon.university.taxicarpool.email.exception;

public class EmailVerificationNotFoundException extends RuntimeException {

    public EmailVerificationNotFoundException(String message) {
        super(message);
    }
}
