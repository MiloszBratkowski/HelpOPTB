package pl.techbrat.spigot.helpop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Report;

public class HelpOPCommand implements CommandExecutor {

    private final ConfigData config = ConfigData.getInstance();

    public HelpOPCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("incorrect_use")));
            return true;
        }
        if (!sender.hasPermission(config.getPerms("reporting"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("no_permission_reporting")));
        } else {
            StringBuilder message = new StringBuilder();
            for (String word :args) {
                message.append(word).append(" ");
            }
            new Report((Player) sender, message.toString());
        }
        return true;
    }
}
