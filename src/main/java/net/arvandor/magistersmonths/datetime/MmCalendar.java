package net.arvandor.magistersmonths.datetime;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MmCalendar {

    private final Instant epochRealTime;
    private final MmDateTime epochInGameTime;
    private final Duration dayLength;
    private final List<MmMonth> months;

    public MmCalendar(Instant epochRealTime, MmDateTime epochInGameTime, Duration dayLength, List<MmMonth> months) {
        this.epochRealTime = epochRealTime;
        this.epochInGameTime = epochInGameTime;
        this.dayLength = dayLength;
        this.months = months;
    }

    public Instant getEpochRealTime() {
        return epochRealTime;
    }

    public MmDateTime getEpochInGameTime() {
        return epochInGameTime;
    }

    public Duration getDayLength() {
        return dayLength;
    }

    public List<MmMonth> getMonths() {
        return months;
    }

    public MmMonth getMonthAt(int dayOfYear) {
        return getMonths().stream()
                .filter(month -> dayOfYear >= month.getStartDay() && dayOfYear <= month.getEndDay())
                .findFirst()
                .orElse(null);
    }

    public MmDateTime toCustomDateTime(Instant instant) {
        Duration timeSinceEpochRealTime = Duration.between(epochRealTime, instant);
        MmDuration timeSinceEpochGameTime = toCustomDuration(timeSinceEpochRealTime);
        return epochInGameTime.plus(timeSinceEpochGameTime);
    }

    private MmDuration toCustomDuration(Duration duration) {
        long seconds = Math.round(duration.toSeconds() * getTimeScale());
        return new MmDuration(seconds);
    }

    private double getTimeScale() {
        return Duration.ofDays(1).dividedBy(dayLength);
    }

    public MmDuration durationBetween(MmDateTime start, MmDateTime end) {
        return new MmDuration(end.toSeconds() - start.toSeconds());
    }

}
