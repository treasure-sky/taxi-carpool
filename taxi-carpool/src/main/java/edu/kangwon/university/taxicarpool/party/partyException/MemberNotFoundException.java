package edu.kangwon.university.taxicarpool.party.partyException;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
