package edu.kangwon.university.taxicarpool.party.partyException;

public class MemberNotInPartyException extends RuntimeException {

    public MemberNotInPartyException(String message) {
        super(message);
    }
}