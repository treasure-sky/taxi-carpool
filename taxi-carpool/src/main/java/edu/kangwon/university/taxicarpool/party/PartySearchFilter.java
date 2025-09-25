package edu.kangwon.university.taxicarpool.party;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartySearchFilter {
    private final Double depLng;
    private final Double depLat;
    private final Double dstLng;
    private final Double dstLat;
    private final LocalDateTime depTime;

    boolean hasDeparture()   { return depLng != null && depLat != null; }
    boolean hasDestination() { return dstLng != null && dstLat != null; }
    boolean hasTime()        { return depTime != null; }
}
