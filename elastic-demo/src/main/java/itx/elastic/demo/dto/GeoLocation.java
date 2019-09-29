package itx.elastic.demo.dto;

public class GeoLocation {

    private final Float longitude;
    private final Float latitude;

    public GeoLocation(Float longitude, Float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

}
