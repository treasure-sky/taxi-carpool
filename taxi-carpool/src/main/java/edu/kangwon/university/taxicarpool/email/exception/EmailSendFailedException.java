package edu.kangwon.university.taxicarpool.email.exception;

public class EmailSendFailedException extends RuntimeException {

    public EmailSendFailedException(String message) {
        super(message);
    }
}
