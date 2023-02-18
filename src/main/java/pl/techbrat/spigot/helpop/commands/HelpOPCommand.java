package pl.techbrat.spigot.helpop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Database;
import pl.techbrat.spigot.helpop.Functions;
import pl.techbrat.spigot.helpop.Report;

import javax.xml.crypto.Data;

public class HelpOPCommand implements CommandExecutor {

    private final ConfigData config = ConfigData.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("incorrect_use")));
            return true;
        }
        if (sender.hasPermission(config.getPerms("history")) && args[0].equals("history")) {
            if(!config.isDatabaseEnabled()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
            else {
                sender.sendMessage((((Player)sender).getUniqueId()).toString());
                Functions.getInstance().displayHistory(sender, 1);
            }
            return true;
        }
        if (sender.hasPermission(config.getPerms("check")) && args[0].equals("check")) {
            if(!config.isDatabaseEnabled()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
            else {
                sender.sendMessage("check");
            }
            return true;
        }
        if (sender.hasPermission(config.getPerms("reload")) && args[0].equals("reload")) {
            new ConfigData();
            if (ConfigData.getInstance().isDatabaseEnabled()) Database.load();
            return true;
        }

        if (!sender.hasPermission(config.getPerms("report"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("no_permission_player")));
        } else {
            StringBuilder message = new StringBuilder();
            for (String word :args) {
                message.append(word).append(" ");
            }
            Report report = new Report((Player) sender, message.toString());
            report.sendReport(true);
        }
        return true;
    }
}
