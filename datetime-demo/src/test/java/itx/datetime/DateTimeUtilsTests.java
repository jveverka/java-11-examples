package itx.datetime;

import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static itx.datetime.DateTimeUtils.getUtcDate;
import static itx.datetime.DateTimeUtils.getUtcInstant;
import static itx.datetime.DateTimeUtils.getUtcLocalDateTime;
import static itx.datetime.DateTimeUtils.toEpochMillis;
import static org.testng.AssertJUnit.assertEquals;

public class DateTimeUtilsTests {

    @Test
    void testingDate() {
        Date originalDate = getUtcDate();
        Long epochMillis = originalDate.getTime();
        Date createdDate = getUtcDate(epochMillis);
        assertEquals(originalDate, createdDate);
        assertEquals(epochMillis, Long.valueOf(createdDate.getTime()));
    }

    @Test
    void testLocalDateTime() {
        LocalDateTime originalLocalDateTime = getUtcLocalDateTime();
        Long epochMillis = toEpochMillis(originalLocalDateTime);
        LocalDateTime createdLocalDateTime = getUtcLocalDateTime(epochMillis);
        assertEquals(originalLocalDateTime.toEpochSecond(ZoneOffset.UTC), createdLocalDateTime.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    void testInstant() {
        Instant originalInstant = getUtcInstant();
        Long epochMillis = originalInstant.toEpochMilli();
        Instant createdInstant = getUtcInstant(epochMillis);
        assertEquals(originalInstant.toEpochMilli(), createdInstant.toEpochMilli());
    }

}
