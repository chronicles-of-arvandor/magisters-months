package net.arvandor.magistersmonths;

import net.arvandor.magistersmonths.command.DateCommand;
import net.arvandor.magistersmonths.datetime.MmCalendar;
import net.arvandor.magistersmonths.datetime.MmDateTime;
import net.arvandor.magistersmonths.datetime.MmMonth;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;

public class MagistersMonths extends JavaPlugin {

    private MmCalendar calendar;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Duration dayLength = Duration.parse(getConfig().getString("day-length"));
        Duration nightLength = Duration.parse(getConfig().getString("night-length"));
        Duration fullDayLength = dayLength.plus(nightLength);
        int sunriseHour = 6;

        calendar = new MmCalendar(
                Instant.parse(getConfig().getString("epoch.real-time")),
                MmDateTime.epoch(
                        getConfig().getInt("epoch.custom-time.year"),
                        getConfig().getInt("epoch.custom-time.day"),
                        getConfig().getInt("epoch.custom-time.hour"),
                        getConfig().getInt("epoch.custom-time.minute"),
                        getConfig().getInt("epoch.custom-time.second")
                ),
                fullDayLength,
                getConfig().getConfigurationSection("months").getKeys(false).stream().map(key -> new MmMonth(
                        key,
                        getConfig().getInt("months." + key + ".start"),
                        getConfig().getInt("months." + key + ".end")
                )).toList()
        );

        getCommand("date").setExecutor(new DateCommand(this));

        getConfig().getStringList("worlds").forEach(worldName -> {
            World world = getServer().getWorld(worldName);
            if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            } else {
                getLogger().warning("World " + worldName + " does not exist.");
            }
        });

        double dayLengthSeconds = ((double) dayLength.getSeconds() / (double) fullDayLength.getSeconds()) * (24.0 * 60.0 * 60.0);
        double nightLengthSeconds = ((double) nightLength.getSeconds() / (double) fullDayLength.getSeconds()) * (24.0 * 60.0 * 60.0);

        getServer().getScheduler().runTaskTimer(this, () -> {
            MmDateTime currentTime = calendar.toCustomDateTime(Instant.now());
            int hour = currentTime.getHour();
            int minute = currentTime.getMinutes();
            int second = currentTime.getSeconds();

            int secondsSinceSunrise = ((hour - sunriseHour) * 3600) + (minute * 60) + second;
            while (secondsSinceSunrise < 0) {
                secondsSinceSunrise += 24 * 3600;
            }

            int time;
            if (secondsSinceSunrise < dayLengthSeconds) {
                // Map to 0 - 12000
                time = (int) ((double) secondsSinceSunrise / dayLengthSeconds * 12000);
            } else {
                // Map to 12000 - 24000
                int secondsIntoNight = secondsSinceSunrise - (int) dayLengthSeconds;
                time = 12000 + (int) ((double) secondsIntoNight / nightLengthSeconds * 12000);
            }
            getServer().getWorlds().forEach(world -> world.setTime(time));
        }, 100L, 100L);
    }

    public MmCalendar getCalendar() {
        return calendar;
    }
}
