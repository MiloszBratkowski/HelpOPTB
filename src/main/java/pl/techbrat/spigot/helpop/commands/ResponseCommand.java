package pl.techbrat.spigot.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Functions;
import pl.techbrat.spigot.helpop.HelpOPTB;
import pl.techbrat.spigot.helpop.bungeecord.BungeePlayerListDownloader;

public class ResponseCommand implements CommandExecutor {

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
            user.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("players.response").replace("<admin>", sender.getName()).replace("<message>", message)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.format").replace("<admin>", sender.getName()).replace("<player>", args[0]).replace("<message>", message)));
            Functions.getInstance().respondedInfo(sender.getName(), args[0], String.valueOf(message));
        } else {
            if (config.isBungeeEnabled()) {
                BungeePlayerListDownloader bungeeList = BungeePlayerListDownloader.getInstance();
                /*
                if (bungeeList.getPlayers().contains(args[0])) {
                    Functions.getInstance().sendResponse(args[0], String.valueOf(message), sender.getName());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.format").replace("<player>", args[0]).replace("<message>", message)));
                } else {*/
                    bungeeList.downloadPlayers((Player) sender);
                    HelpOPTB.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                        if (bungeeList.getPlayers().contains(args[0])) {
                            Functions.getInstance().sendResponse(args[0], String.valueOf(message), sender.getName());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.format").replace("<admin>", sender.getName()).replace("<player>", args[0]).replace("<message>", message)));
                            Functions.getInstance().respondedInfo(sender.getName(), args[0], String.valueOf(message));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.offline_player").replace("<player>", args[0])));
                        }
                    }, 2);
                //}
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.commands.response.offline_player").replace("<player>", args[0])));
            }
            return true;
        }
        return true;
    }
}
