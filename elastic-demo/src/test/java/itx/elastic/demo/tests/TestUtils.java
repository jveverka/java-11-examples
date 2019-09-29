package itx.elastic.demo.tests;

import itx.elastic.demo.Utils;
import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventDataInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class TestUtils {

    private TestUtils() {
    }

    public final static String INDEX_NAME = "events";

    public static EventData createEventData() {
        return Utils.createEventData(UUID.randomUUID().toString(), "event1", "special event 1", 45.5F, 55.4F, 123456987L, new String[] {});
    }

    public static EventData createEventData(int counter) {
        return Utils.createEventData(UUID.randomUUID().toString(), "event" + counter, "special event " + counter, 45.5F + counter, 55.4F + counter, 123456987L + counter, new String[] { "event" + (counter - 1) });
    }

    public static EventDataInfo createEventDataInfo(int counter) {
        return Utils.createEventDataInfo("event" + counter, "special event " + counter, 45.5F + counter, 55.4F + counter, 123456987L + counter, new String[] { "event" + (counter - 1) });
    }

    public static Map<String, Object> createSourceData() {
        Map<String, Object> source = new HashMap<>();
        Map<String, Object> id = new HashMap<>();
        id.put("id", "id123");
        Map<String, Object> geoLocation = new HashMap<>();
        geoLocation.put("lon", "45.23");
        geoLocation.put("lat", "54.13");
        List<Object> related = new ArrayList<>();
        related.add(id);
        source.put("id", id);
        source.put("name", "event name");
        source.put("description", "event description");
        source.put("geoLocation", geoLocation);
        source.put("timeStamp", "123456798");
        source.put("related", related);
        return source;
    }

}
