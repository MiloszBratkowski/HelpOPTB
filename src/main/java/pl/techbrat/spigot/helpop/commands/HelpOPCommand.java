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
        if(sender.hasPermission(config.getPerms("help")) && args[0].equals("help")) {
            Functions.getInstance().displayHelp(sender);
            return true;
        }
        if (sender.hasPermission(config.getPerms("history")) && args[0].equals("history")) {
            if(!config.isDatabaseEnabled()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
            else {
                final int TYPE = 0; //TODO in the future to implement
                int page = 1;
                if(args.length > 1) {
                    if (Functions.getInstance().isInteger(args[1])) {
                        page = Integer.parseInt(args[1]);
                        final int all_pages = Functions.getInstance().getNumbersOfPages(TYPE);
                        if (page <= 0 || page > all_pages) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("page_rage").replace("<all_pages>", Integer.toString(all_pages))));
                            return true;
                        }
                    }
                }
                Functions.getInstance().displayHistory(sender, TYPE, page);
            }
            return true;
        }
        if (sender.hasPermission(config.getPerms("check")) && args[0].equals("check")) {
            if(!config.isDatabaseEnabled()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
            else {
                if (args.length<2 || !Functions.getInstance().isInteger(args[1])) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("check_type_id")));
                    return true;
                }
                int id = Integer.parseInt(args[1]);
                //TODO global hashmap with reports to identify them by id (hashmap's key)
                sender.sendMessage("check ");
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
            for (String word : args) {
                message.append(word).append(" ");
            }
            Report report = new Report((Player) sender, message.toString());
            report.sendReport(true);
        }
        return true;
    }
}
