package edu.kangwon.university.taxicarpool.map;

import java.util.List;

public class MapSearchResponseDTO {

    private final List<MapPlaceDTO> places;

    public MapSearchResponseDTO(List<MapPlaceDTO> places) {
        this.places = places;
    }

    public List<MapPlaceDTO> getPlaces() {
        return places;
    }
}
