package edu.kangwon.university.taxicarpool.party.partyException;

public class PartyNotFoundException extends RuntimeException {
    public PartyNotFoundException(String message) {
        super(message);
    }
}