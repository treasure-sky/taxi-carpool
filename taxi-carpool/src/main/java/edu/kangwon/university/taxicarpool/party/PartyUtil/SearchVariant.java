package edu.kangwon.university.taxicarpool.party.PartyUtil;

public enum SearchVariant {
    ALL,
    NO_DEPARTURE,
    NO_DESTINATION,
    NO_TIME;

    public static SearchVariant fromFilter(PartySearchFilter f) {
        if (f.hasDeparture() && f.hasDestination() && f.hasTime()) return ALL;
        if (!f.hasDeparture())   return NO_DEPARTURE;
        if (!f.hasDestination()) return NO_DESTINATION;
        return NO_TIME;
    }
}
