package itx.elastic.demo.dto;

import java.util.List;

public class EventData {

    private final EventId id;
    private final String name;
    private final String description;
    private final List<EventId> related;
    private final GeoLocation geoLocation;
    private final long timeStamp;

    public EventData(EventId id, String name, String description, List<EventId> related,
                     GeoLocation geoLocation, long timeStamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.related = related;
        this.geoLocation = geoLocation;
        this.timeStamp = timeStamp;
    }

    public EventData(EventId id, EventDataInfo eventDataInfo) {
        this.id = id;
        this.name = eventDataInfo.getName();
        this.description = eventDataInfo.getDescription();
        this.related = eventDataInfo.getRelated();
        this.geoLocation = eventDataInfo.getGeoLocation();
        this.timeStamp = eventDataInfo.getTimeStamp();
    }

    public EventId getId() {
        return id;
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
