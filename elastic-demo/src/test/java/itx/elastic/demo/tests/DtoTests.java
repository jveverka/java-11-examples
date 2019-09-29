package itx.elastic.demo.tests;

import itx.elastic.demo.dto.EventId;
import org.testng.Assert;
import org.testng.annotations.Test;

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

}
