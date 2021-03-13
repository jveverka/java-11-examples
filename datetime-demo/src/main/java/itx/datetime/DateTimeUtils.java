package itx.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public final  class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static Long getUtcTimeStamp() {
        //System.currentTimeMillis();
        return Instant.now().toEpochMilli();
    }

    public static Date getUtcDate() {
        return Date.from(Instant.now());
    }

    public static Date getUtcDate(Long epochTime) {
        return Date.from(Instant.ofEpochMilli(epochTime));
    }

    public static LocalDateTime getUtcLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }

    public static LocalDateTime getUtcLocalDateTime(Long epochTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTime), ZoneOffset.UTC);
    }

    public static Instant getUtcInstant() {
        return Instant.now();
    }

    public static Instant getUtcInstant(Long epochTime) {
        return Instant.ofEpochMilli(epochTime);
    }

    public static Long toEpochMillis(LocalDateTime localDateTime)  {
        return localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

}
