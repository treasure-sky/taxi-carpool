package edu.kangwon.university.taxicarpool.map;

public class MapPlaceDTO {

    public MapPlaceDTO(String name, String road_address_name, String x, String y) {
        this.name = name;
        this.roadAddressName = road_address_name;
        this.x = x;
        this.y = y;
    }

    private final String name;
    private final String roadAddressName;
    private final String x;
    private final String y;

    public String getName() {
        return name;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
