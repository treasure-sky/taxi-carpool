package edu.kangwon.university.taxicarpool.map;

public class MapPlaceDTO {

    public MapPlaceDTO(String name, String road_address_name, double x, double y) {
        this.name = name;
        this.roadAddressName = road_address_name;
        this.x = x;
        this.y = y;
    }

    private final String name;
    private final String roadAddressName;
    private final double x;
    private final double y;

    public String getName() {
        return name;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
