package edu.kangwon.university.taxicarpool.map;

public class MapPlaceDTO {

    public MapPlaceDTO(String name, String road_address_name, String x, String y) {
        this.name = name;
        this.road_address_name = road_address_name;
        this.x = x;
        this.y = y;
    }

    private final String name;
    private final String road_address_name;
    private final String x;
    private final String y;

    public String getName() {
        return name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
