package net.arvandor.magistersmonths.command;

import net.arvandor.magistersmonths.MagistersMonths;
import net.arvandor.magistersmonths.datetime.MmCalendar;
import net.arvandor.magistersmonths.datetime.MmDateTime;
import net.arvandor.magistersmonths.datetime.MmMonth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Instant;

public class DateCommand implements CommandExecutor {

    private final MagistersMonths plugin;

    public DateCommand(MagistersMonths plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MmCalendar calendar = plugin.getCalendar();
        MmDateTime gameTime = calendar.toCustomDateTime(Instant.now());
        MmMonth month = calendar.getMonthAt(gameTime.getDayOfYear());
        int dayOfMonth = month != null ? (gameTime.getDayOfYear() - calendar.getMonthAt(gameTime.getDayOfYear()).getStartDay()) : 0;
        sender.sendMessage("Date: " + (month != null ? (dayOfMonth + " " + month.getName()) : gameTime.getDayOfYear()) + " " + gameTime.getYear() + " " + gameTime.getHour() + ":" + gameTime.getMinutes() + ":" + gameTime.getSeconds());
        return true;
    }

}
