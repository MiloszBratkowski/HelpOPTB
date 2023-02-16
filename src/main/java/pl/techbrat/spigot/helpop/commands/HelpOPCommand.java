package pl.techbrat.spigot.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.ArrayList;

public class HelpOPCommand implements CommandExecutor {

    private final HelpOPTB plugin = HelpOPTB.getInstance();

    public HelpOPCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("incorrect_use")));
            return true;
        }
        if (!sender.hasPermission(plugin.getConfig().getString("permissions.reporting"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("permissions.no_permission_reporting")));
            return true;
        } else {
            String permission = plugin.getConfig().getString("permissions.receiving");

            String chatmessage = plugin.getConfig().getString("admin_message_format");
            String message = "";
            for (String word :args) {
                message+=word+" ";
            }
            chatmessage = chatmessage.replace("<message>", message);
            chatmessage = chatmessage.replace("<player>", sender.getName());
            chatmessage = ChatColor.translateAlternateColorCodes('&', chatmessage);

            String titlemessage = "";
            String subtitlemessage = "";
            if(plugin.getConfig().getBoolean("screen_information")) {
                titlemessage = plugin.getConfig().getString("screen_title");
                titlemessage = titlemessage.replace("<message>", message);
                titlemessage = titlemessage.replace("<player>", sender.getName());
                titlemessage = ChatColor.translateAlternateColorCodes('&', titlemessage);
                subtitlemessage = plugin.getConfig().getString("screen_subtitle");
                subtitlemessage = subtitlemessage.replace("<message>", message);
                subtitlemessage = subtitlemessage.replace("<player>", sender.getName());
                subtitlemessage = ChatColor.translateAlternateColorCodes('&', subtitlemessage);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(permission)) {
                    player.sendMessage(chatmessage);
                    if(plugin.getConfig().getBoolean("screen_information")) {
                        player.sendTitle(titlemessage, subtitlemessage);
                    }
                }
            };
            return true;
        }
    }
}
