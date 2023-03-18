package pl.techbrat.spigot.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Functions;

public class ReponseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigData config = ConfigData.getInstance();
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.type_player")));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.type_message")));
            return true;
        }
        Player user = Bukkit.getPlayer(args[0]);
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        if (user != null && user.isOnline()) {
            user.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("players.response").replace("<player>", sender.getName()).replace("<message>", message)));
        } else {
            Functions.getInstance().sendResponse(args[0], String.valueOf(message), sender.getName());
        }
        return true;
    }
}
