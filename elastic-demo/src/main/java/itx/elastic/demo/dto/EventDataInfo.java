package itx.elastic.demo.dto;

import java.util.List;

public class EventDataInfo {

    private final String name;
    private final String description;
    private final List<EventId> related;
    private final GeoLocation geoLocation;
    private final long timeStamp;

    public EventDataInfo(String name, String description, List<EventId> related, GeoLocation geoLocation, long timeStamp) {
        this.name = name;
        this.description = description;
        this.related = related;
        this.geoLocation = geoLocation;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<EventId> getRelated() {
        return related;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
