package edu.kangwon.university.taxicarpool.party.PartyUtil;

import edu.kangwon.university.taxicarpool.party.partyException.PartyGetCustomException;
import java.time.LocalDateTime;

public class PartySearchValidator {

    private PartySearchValidator() {}

    public static void validate(PartySearchFilter f) {
        if (f.hasTime() && !f.getDepTime().isAfter(LocalDateTime.now())) {
            throw new PartyGetCustomException("출발 시간은 현재 시간보다 이후여야 합니다.");
        }

        int missing = 0;
        if (!f.hasDeparture())   missing++;
        if (!f.hasDestination()) missing++;
        if (!f.hasTime())        missing++;

        if (missing >= 2) {
            throw new PartyGetCustomException("출발지, 도착지, 출발시간 중 최소 2개는 제공되어야 합니다.");
        }
    }
}