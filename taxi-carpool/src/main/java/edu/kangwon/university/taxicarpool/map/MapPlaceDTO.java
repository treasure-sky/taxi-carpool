package edu.kangwon.university.taxicarpool.map;

public class MapPlaceDTO {

    public MapPlaceDTO(String name, String road_address_name, Double x, Double y) {
        this.name = name;
        this.roadAddressName = road_address_name;
        this.x = x;
        this.y = y;
    }

    private final String name;
    private final String roadAddressName;
    private final Double x;
    private final Double y;

    public String getName() {
        return name;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
