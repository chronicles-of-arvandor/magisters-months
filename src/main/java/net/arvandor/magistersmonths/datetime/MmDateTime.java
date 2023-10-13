package net.arvandor.magistersmonths.datetime;

public class MmDateTime {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    private static final int DAYS_PER_YEAR = 375;
    private static final int SECONDS_PER_YEAR = SECONDS_PER_DAY * DAYS_PER_YEAR;

    private final MmDateTime epoch;
    private final int year;
    private final int dayOfYear;
    private final int hour;
    private final int minutes;
    private final int seconds;

    public MmDateTime(MmDateTime epoch, int year, int dayOfYear, int hour, int minutes, int seconds) {
        this.epoch = epoch;
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    private MmDateTime(int year, int dayOfYear, int hour, int minutes, int seconds) {
        this.epoch = this;
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public static MmDateTime epoch(int year, int dayOfYear, int hour, int minutes, int seconds) {
        return new MmDateTime(year, dayOfYear, hour, minutes, seconds);
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
        int yearsAsSeconds = (getYear() - epoch.getYear()) * SECONDS_PER_YEAR;
        int daysAsSeconds = (getDayOfYear() - epoch.getDayOfYear()) * SECONDS_PER_DAY;
        int hoursAsSeconds = (getHour() - epoch.getHour()) * SECONDS_PER_HOUR;
        int minutesAsSeconds = (getMinutes() - epoch.getMinutes()) * SECONDS_PER_MINUTE;
        return yearsAsSeconds + daysAsSeconds + hoursAsSeconds + minutesAsSeconds + getSeconds();
    }

    public MmDateTime plus(MmDuration duration) {
        return MmDateTime.fromSeconds(epoch, toSeconds() + duration.getSeconds());
    }

    public static MmDateTime fromSeconds(MmDateTime epoch, long seconds) {
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

        return new MmDateTime(
                epoch,
                epoch.getYear() + yearsSinceEpoch,
                epoch.getDayOfYear() + daysOfYearSinceEpoch,
                epoch.getHour() + hourOfDaySinceEpoch,
                epoch.getMinutes() + minuteOfHourSinceEpoch,
                remainingSeconds
        );
    }

}
