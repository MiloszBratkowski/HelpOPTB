package pl.techbrat.spigot.helpop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.*;

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
            if(!config.isDatabaseEnabled()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
                return true;
            }
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
            return true;
        }
        if (sender.hasPermission(config.getPerms("check")) && args[0].equals("check")) {
            if(!config.isDatabaseEnabled()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
                return true;
            }
            if (args.length<2 || !Functions.getInstance().isInteger(args[1])) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("check_type_id")));
                return true;
            }
            int id = Integer.parseInt(args[1]);
            if (!DatabaseReportManager.getInstance().containsId(id)) {
                if (DatabaseReportManager.getInstance().softSolve(id, sender.getName())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("check_report")));
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("incorrect_report_id")));
                return true;
            }

            if (DatabaseReportManager.getInstance().getReport(id).isSolved()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("report_is_solved")));
                return true;
            }
            DatabaseReportManager.getInstance().getReport(id).solveReport(sender.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("check_report")));
            return true;
        }
        if (sender.hasPermission(config.getPerms("clear.all")) && args[0].equals("clear_all")) {
            if(!config.isDatabaseEnabled()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
                return true;
            }
            DatabaseReportManager.getInstance().clearReports(0);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("clearing_reports")));
            return true;
        }
        if (sender.hasPermission(config.getPerms("clear.solved")) && args[0].equals("clear_solved")) {
            if(!config.isDatabaseEnabled()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("disabled_database")));
                return true;
            }
            DatabaseReportManager.getInstance().clearReports(2);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("clearing_reports")));
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
