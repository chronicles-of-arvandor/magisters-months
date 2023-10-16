package net.arvandor.magistersmonths.datetime;

import net.arvandor.magistersmonths.MagistersMonths;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MmCalendar {

    private final Instant epochRealTime;
    private final MmDateTime epochInGameTime;
    private final Duration dayLength;
    private final List<MmMonth> months;

    public MmCalendar(MagistersMonths plugin) {
        Duration dayLength = Duration.parse(plugin.getConfig().getString("day-length"));
        Duration nightLength = Duration.parse(plugin.getConfig().getString("night-length"));
        Duration fullDayLength = dayLength.plus(nightLength);

        this.epochRealTime = Instant.parse(plugin.getConfig().getString("epoch.real-time"));
        this.epochInGameTime = new MmDateTime(
            this,
            plugin.getConfig().getInt("epoch.custom-time.year"),
            plugin.getConfig().getInt("epoch.custom-time.day"),
            plugin.getConfig().getInt("epoch.custom-time.hour"),
            plugin.getConfig().getInt("epoch.custom-time.minute"),
            plugin.getConfig().getInt("epoch.custom-time.second")
        );
        this.dayLength = fullDayLength;
        this.months = plugin.getConfig().getConfigurationSection("months").getKeys(false).stream().map(key -> new MmMonth(
            key,
            plugin.getConfig().getInt("months." + key + ".start"),
            plugin.getConfig().getInt("months." + key + ".end")
        )).toList();
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

    public MmDateTime toMmDateTime(Instant instant) {
        Duration timeSinceEpochRealTime = Duration.between(epochRealTime, instant);
        MmDuration timeSinceEpochGameTime = toMmDuration(timeSinceEpochRealTime);
        return epochInGameTime.plus(timeSinceEpochGameTime);
    }

    private MmDuration toMmDuration(Duration duration) {
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
