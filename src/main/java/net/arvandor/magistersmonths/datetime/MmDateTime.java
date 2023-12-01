package net.arvandor.magistersmonths.datetime;

import java.util.Comparator;

public class MmDateTime {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    private static final int DAYS_PER_YEAR = 365;
    private static final int SECONDS_PER_YEAR = SECONDS_PER_DAY * DAYS_PER_YEAR;

    private final MmCalendar calendar;
    private final int year;
    private final int dayOfYear;
    private final int hour;
    private final int minutes;
    private final int seconds;

    public MmDateTime(MmCalendar calendar, int year, int dayOfYear, int hour, int minutes, int seconds) {
        this.calendar = calendar;
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getYear() {
        return year;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int toSeconds() {
        int yearsAsSeconds = (getYear() - calendar.getEpochInGameTime().getYear()) * SECONDS_PER_YEAR;
        int daysAsSeconds = (getDayOfYear() - calendar.getEpochInGameTime().getDayOfYear()) * SECONDS_PER_DAY;
        int hoursAsSeconds = (getHour() - calendar.getEpochInGameTime().getHour()) * SECONDS_PER_HOUR;
        int minutesAsSeconds = (getMinutes() - calendar.getEpochInGameTime().getMinutes()) * SECONDS_PER_MINUTE;
        return yearsAsSeconds + daysAsSeconds + hoursAsSeconds + minutesAsSeconds + getSeconds();
    }

    public MmDateTime plus(MmDuration duration) {
        return MmDateTime.fromSeconds(calendar, calendar.getEpochInGameTime(), toSeconds() + duration.getSeconds());
    }

    public static MmDateTime fromSeconds(MmCalendar calendar, MmDateTime epoch, long seconds) {
        int yearsSinceEpoch = (int) (seconds / SECONDS_PER_YEAR);
        int remainingSeconds = (int) (seconds % SECONDS_PER_YEAR);

        if (remainingSeconds < 0) {
            yearsSinceEpoch--;
            remainingSeconds += SECONDS_PER_YEAR;
        }

        int daysOfYearSinceEpoch = remainingSeconds / SECONDS_PER_DAY;
        remainingSeconds %= SECONDS_PER_DAY;

        if (remainingSeconds < 0) {
            daysOfYearSinceEpoch--;
            remainingSeconds += SECONDS_PER_DAY;
        }

        int hourOfDaySinceEpoch = remainingSeconds / SECONDS_PER_HOUR;
        remainingSeconds %= SECONDS_PER_HOUR;

        if (remainingSeconds < 0) {
            hourOfDaySinceEpoch--;
            remainingSeconds += SECONDS_PER_HOUR;
        }

        int minuteOfHourSinceEpoch = remainingSeconds / SECONDS_PER_MINUTE;
        remainingSeconds %= SECONDS_PER_MINUTE;

        if (remainingSeconds < 0) {
            minuteOfHourSinceEpoch--;
            remainingSeconds += SECONDS_PER_MINUTE;
        }

        int newMinutes = epoch.getMinutes() + minuteOfHourSinceEpoch;
        int overflowFromMinutes = newMinutes / 60;
        newMinutes %= 60;

        int newHours = epoch.getHour() + hourOfDaySinceEpoch + overflowFromMinutes;
        int overflowFromHours = newHours / 24;
        newHours %= 24;

        int newDays = epoch.getDayOfYear() + daysOfYearSinceEpoch + overflowFromHours;
        int daysPerYear = calendar.getMonths().stream().max(Comparator.comparingInt(MmMonth::getEndDay)).map(MmMonth::getEndDay).orElse(0) + 1;
        int overflowFromDays = newDays / daysPerYear;
        newDays %= daysPerYear;

        if (newDays < 0) {
            overflowFromDays--;
            newDays += daysPerYear;
        }

        int newYears = epoch.getYear() + yearsSinceEpoch + overflowFromDays;

        return new MmDateTime(
                calendar,
                newYears,
                newDays,
                newHours,
                newMinutes,
                remainingSeconds
        );
    }

}
