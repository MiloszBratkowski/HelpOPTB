package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report {

    private ConfigData config = ConfigData.getInstance();

    public Report(Player player, String message) {
        ArrayList<Player> admins = new ArrayList<>();
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer.hasPermission(config.getPerms("receive"))) admins.add(loopPlayer);
        }
        if(admins.size() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("no_admins")));
            return;
        }

        String chatmessage = config.getStyles("admin_message_format");
        chatmessage = chatmessage.replace("<message>", message);
        chatmessage = chatmessage.replace("<player>", player.getName());
        chatmessage = ChatColor.translateAlternateColorCodes('&', chatmessage);

        String titlemessage = "";
        String subtitlemessage = "";
        if(config.isScreenEnabled()) {
            titlemessage = config.getStyles("screen_title");
            titlemessage = titlemessage.replace("<message>", message);
            titlemessage = titlemessage.replace("<player>", player.getName());
            titlemessage = ChatColor.translateAlternateColorCodes('&', titlemessage);
            subtitlemessage = config.getStyles("screen_subtitle");
            subtitlemessage = subtitlemessage.replace("<message>", message);
            subtitlemessage = subtitlemessage.replace("<player>", player.getName());
            subtitlemessage = ChatColor.translateAlternateColorCodes('&', subtitlemessage);
        }

        for (Player admin : admins) {
            admin.sendMessage(chatmessage);
            if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                admin.sendTitle(titlemessage, subtitlemessage);
            }
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("feedback")));
    }
}
