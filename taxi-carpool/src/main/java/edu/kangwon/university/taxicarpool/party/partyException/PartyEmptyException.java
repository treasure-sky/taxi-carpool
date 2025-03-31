package edu.kangwon.university.taxicarpool.party.partyException;

public class PartyEmptyException extends RuntimeException {

    public PartyEmptyException(String message) {
        super(message);
    }
}