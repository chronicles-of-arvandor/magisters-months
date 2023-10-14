package net.arvandor.magistersmonths.command;

import net.arvandor.magistersmonths.MagistersMonths;
import net.arvandor.magistersmonths.datetime.MmCalendar;
import net.arvandor.magistersmonths.datetime.MmDateTime;
import net.arvandor.magistersmonths.datetime.MmMonth;
import net.md_5.bungee.api.ChatColor;
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
        MmDateTime gameTime = calendar.toMmDateTime(Instant.now());
        MmMonth month = calendar.getMonthAt(gameTime.getDayOfYear());
        int dayOfMonth = month != null ? ((gameTime.getDayOfYear() - calendar.getMonthAt(gameTime.getDayOfYear()).getStartDay()) + 1) : 0;
        sender.sendMessage(ChatColor.GOLD + "Date: " +
                (month != null ? (dayOfMonth + " " + month.getName()) : gameTime.getDayOfYear()) + " " +
                gameTime.getYear() + " " +
                gameTime.getHour() + ":" +
                String.format("%02d", gameTime.getMinutes()) + ":" +
                String.format("%02d", gameTime.getSeconds()));
        return true;
    }

}
