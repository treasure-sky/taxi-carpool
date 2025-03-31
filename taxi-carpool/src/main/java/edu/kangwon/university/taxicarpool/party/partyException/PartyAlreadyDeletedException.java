package edu.kangwon.university.taxicarpool.party.partyException;

public class PartyAlreadyDeletedException extends RuntimeException {

    public PartyAlreadyDeletedException(String message) {
        super(message);
    }
}