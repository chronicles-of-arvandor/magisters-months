package net.arvandor.magistersmonths.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.arvandor.magistersmonths.MagistersMonths;
import net.arvandor.magistersmonths.datetime.MmCalendar;
import net.arvandor.magistersmonths.datetime.MmDateTime;
import net.arvandor.magistersmonths.datetime.MmMonth;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class MmPlaceholderExpansion extends PlaceholderExpansion {

    private final MagistersMonths plugin;

    public MmPlaceholderExpansion(MagistersMonths plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        MmCalendar calendar = plugin.getCalendar();
        MmDateTime gameTime = calendar.toMmDateTime(Instant.now());
        MmMonth month = calendar.getMonthAt(gameTime.getDayOfYear());
        int dayOfMonth = month != null ? (gameTime.getDayOfYear() - calendar.getMonthAt(gameTime.getDayOfYear()).getStartDay()) : 0;
        if (params.equalsIgnoreCase("date")) {
            return (month != null ? (dayOfMonth + " " + month.getName()) : gameTime.getDayOfYear()) + " " + gameTime.getYear();
        } else if (params.equalsIgnoreCase("time")) {
            return gameTime.getHour() + ":" +
                    String.format("%02d", gameTime.getMinutes()) + ":" +
                    String.format("%02d", gameTime.getSeconds());
        } else if (params.equalsIgnoreCase("datetime")) {
            return (month != null ? (dayOfMonth + " " + month.getName()) : gameTime.getDayOfYear()) + " " +
                    gameTime.getYear() + " " +
                    gameTime.getHour() + ":" +
                    String.format("%02d", gameTime.getMinutes()) + ":" +
                    String.format("%02d", gameTime.getSeconds());
        } else if (params.equalsIgnoreCase("year")) {
            return String.valueOf(gameTime.getYear());
        } else if (params.equalsIgnoreCase("dayofyear")) {
            return String.valueOf(gameTime.getDayOfYear());
        } else if (params.equalsIgnoreCase("day")) {
            return String.valueOf(dayOfMonth);
        } else if (params.equalsIgnoreCase("month")) {
            return month != null ? month.getName() : "";
        } else if (params.equalsIgnoreCase("hour")) {
            return String.valueOf(gameTime.getHour());
        } else if (params.equalsIgnoreCase("minute")) {
            return String.format("%02d", gameTime.getMinutes());
        } else if (params.equalsIgnoreCase("second")) {
            return String.format("%02d", gameTime.getSeconds());
        } else {
            return null;
        }
    }
}
