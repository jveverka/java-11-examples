package itx.elastic.demo.tests;

import itx.elastic.demo.Utils;
import itx.elastic.demo.dto.EventData;
import itx.elastic.demo.dto.EventId;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class DtoTests {

    @Test
    public void testEventId() {
        EventId eventId1 = new EventId("01");
        EventId eventId2 = new EventId("02");
        EventId eventId3 = new EventId("01");

        Assert.assertTrue(eventId1.equals(eventId3));
        Assert.assertTrue(eventId3.equals(eventId1));
        Assert.assertTrue(eventId3.equals(eventId3));
        Assert.assertFalse(eventId1.equals(eventId2));
        Assert.assertFalse(eventId2.equals(eventId1));
    }

    @Test
    public void serializationTest() throws IOException {
        XContentBuilder content = null;
        content = Utils.createContent(TestUtils.createEventData(0));
        Assert.assertNotNull(content);
        content = Utils.createContent(TestUtils.createEventData(1));
        Assert.assertNotNull(content);
    }

    @Test
    public void deserializationTest() {
        EventData eventData = Utils.createFromSource("01", TestUtils.createSourceData());
        Assert.assertNotNull(eventData);
        Assert.assertNotNull(eventData.getId());
        Assert.assertNotNull(eventData.getId().getId());
        Assert.assertNotNull(eventData.getName());
        Assert.assertNotNull(eventData.getDescription());
        Assert.assertNotNull(eventData.getGeoLocation());
        Assert.assertNotNull(eventData.getRelated());
    }

}
