package itx.elastic.demo;

import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventDataInfo;
import itx.elastic.demo.dto.EventId;
import itx.elastic.demo.dto.GeoLocation;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Utils {

    private Utils() {
    }

    public static XContentBuilder createMapping() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                builder.startObject("description");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                builder.startObject("geoLocation");
                {
                    builder.field("type", "geo_point");
                }
                builder.endObject();
                builder.startObject("timeStamp");
                {
                    builder.field("type", "date");
                }
                builder.endObject();
                builder.startObject("related");
                {
                    builder.field("type", "nested");
                    builder.startObject("properties");
                    {
                        builder.startObject("id");
                        {
                            builder.field("type", "keyword");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        return builder;
    }

    public static XContentBuilder createContent(EventData eventData) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("name", eventData.getName());
            builder.field("description", eventData.getDescription());
            builder.field("timeStamp", eventData.getTimeStamp());
            builder.startObject("geoLocation");
            {
                builder.field("lon", eventData.getGeoLocation().getLongitude());
                builder.field("lat", eventData.getGeoLocation().getLatitude());
            }
            builder.endObject();
            builder.startArray("related");
            {
                for (EventId event: eventData.getRelated()) {
                    builder.startObject();
                    {
                        builder.field("id", event.getId());
                    }
                    builder.endObject();
                }
            }
            builder.endArray();
        }
        builder.endObject();
        return builder;
    }

    public static XContentBuilder createContent(EventDataInfo eventDataInfo) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("name", eventDataInfo.getName());
            builder.field("description", eventDataInfo.getDescription());
            builder.field("timeStamp", eventDataInfo.getTimeStamp());
            builder.startObject("geoLocation");
            {
                builder.field("lon", eventDataInfo.getGeoLocation().getLongitude());
                builder.field("lat", eventDataInfo.getGeoLocation().getLatitude());
            }
            builder.endObject();
            builder.startArray("related");
            {
                for (EventId event: eventDataInfo.getRelated()) {
                    builder.startObject();
                    {
                        builder.field("id", event.getId());
                    }
                    builder.endObject();
                }
            }
            builder.endArray();
        }
        builder.endObject();
        return builder;
    }

    public static EventData createEventData(String idString, String name, String description, float longitude, float latitude, long timeStamp, String ... relatedIds) {
        EventId id = new EventId(idString);
        GeoLocation geoLocation = new GeoLocation(longitude, latitude);
        List<EventId> related = new ArrayList<>();
        if (relatedIds != null) {
            for (String relatedId : relatedIds) {
                related.add(new EventId(relatedId));
            }
        }
        return new EventData(id, name, description, related, geoLocation, timeStamp);
    }

    public static EventDataInfo createEventDataInfo(String name, String description, float longitude, float latitude, long timeStamp, String ... relatedIds) {
        GeoLocation geoLocation = new GeoLocation(longitude, latitude);
        List<EventId> related = new ArrayList<>();
        if (relatedIds != null) {
            for (String relatedId : relatedIds) {
                related.add(new EventId(relatedId));
            }
        }
        return new EventDataInfo(name, description, related, geoLocation, timeStamp);
    }

    @SuppressWarnings("unchecked")
    public static EventData createFromSource(String id, Map<String, Object> source) {
        Map<String, Object> geoLocationMap = (Map<String, Object>)source.get("geoLocation");
        EventId eventId = new EventId(id);
        List<EventId> related = new ArrayList<>();
        List<Object> relatedMap = (List<Object>)source.get("related");
        relatedMap.forEach(v->{
            Map<String, Object> relatedId = (Map<String, Object>)v;
            related.add(new EventId(relatedId.get("id").toString()));
        });
        GeoLocation geoLocation = new GeoLocation(Float.parseFloat(geoLocationMap.get("lon").toString()), Float.parseFloat(geoLocationMap.get("lat").toString()));
        return new EventData(eventId, source.get("name").toString(),
                source.get("description").toString(), related, geoLocation, Long.parseLong(source.get("timeStamp").toString()));
    }

}
