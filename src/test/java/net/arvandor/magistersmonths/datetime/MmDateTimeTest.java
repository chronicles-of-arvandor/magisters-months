package net.arvandor.magistersmonths.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MmDateTimeTest {

    private final MmCalendar calendar = new MmCalendar(Duration.ofHours(12), Duration.ofHours(12), Instant.now(), new MmDateTime(null, 1, 1, 0, 0, 0), List.of(
            new MmMonth("Hammer", 1, 30),
            new MmMonth("Midwinter", 31, 31),
            new MmMonth("Alturiak", 32, 61),
            new MmMonth("Ches", 62, 91),
            new MmMonth("Tarsakh", 92, 121),
            new MmMonth("Greengrass", 122, 122),
            new MmMonth("Mirtul", 123, 152),
            new MmMonth("Kythorn", 153, 182),
            new MmMonth("Flamerule", 183, 212),
            new MmMonth("Midsummer", 213, 213),
            new MmMonth("Eleasis", 214, 243),
            new MmMonth("Eleint", 244, 273),
            new MmMonth("Highharvestide", 274, 274),
            new MmMonth("Marpenoth", 275, 304),
            new MmMonth("Uktar", 305, 334),
            new MmMonth("Feast of the Moon", 335, 335),
            new MmMonth("Nightal", 336, 365)
    ));
    private final MmDateTime epoch = new MmDateTime(calendar, 1, 1, 0, 0, 0);

    @Test
    public void test1HourSinceEpoch() {
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 3600);

        assertEquals(1, dateTime.getYear());
        assertEquals(1, dateTime.getDayOfYear());
        assertEquals(1, dateTime.getHour());
        assertEquals(0, dateTime.getMinutes());
        assertEquals(0, dateTime.getSeconds());
    }

    @Test
    public void test1DaySinceEpoch() {
        // 24 hours (1 day) = 86400 seconds
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 86400);

        assertEquals(1, dateTime.getYear());
        assertEquals(2, dateTime.getDayOfYear());
        assertEquals(0, dateTime.getHour());
        assertEquals(0, dateTime.getMinutes());
        assertEquals(0, dateTime.getSeconds());
    }

    @Test
    public void test1YearSinceEpoch() {
        // 365 days = 31536000 seconds
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 31536000);

        assertEquals(2, dateTime.getYear());
        assertEquals(1, dateTime.getDayOfYear());
        assertEquals(0, dateTime.getHour());
        assertEquals(0, dateTime.getMinutes());
        assertEquals(0, dateTime.getSeconds());
    }

    @Test
    public void test1AndAHalfYearsSinceEpoch() {
        // 547.5 days = 47304000 seconds
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 47304000);

        assertEquals(2, dateTime.getYear());
        assertEquals(183, dateTime.getDayOfYear());
        assertEquals(12, dateTime.getHour());
        assertEquals(0, dateTime.getMinutes());
        assertEquals(0, dateTime.getSeconds());
    }

    @Test
    public void testArbitraryTime() {
        // 2000 seconds = 33 minutes and 20 seconds
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 2000);

        assertEquals(1, dateTime.getYear());
        assertEquals(1, dateTime.getDayOfYear());
        assertEquals(0, dateTime.getHour());
        assertEquals(33, dateTime.getMinutes());
        assertEquals(20, dateTime.getSeconds());
    }

    @Test
    public void testToSecondsReversible() {
        MmDateTime dateTime = MmDateTime.fromSeconds(calendar, epoch, 2000);
        assertEquals(2000, dateTime.toSeconds());
    }

}