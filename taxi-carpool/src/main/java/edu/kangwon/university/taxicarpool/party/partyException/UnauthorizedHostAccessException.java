package edu.kangwon.university.taxicarpool.party.partyException;

public class UnauthorizedHostAccessException extends RuntimeException {
    public UnauthorizedHostAccessException(String message) {
        super(message);
    }
}