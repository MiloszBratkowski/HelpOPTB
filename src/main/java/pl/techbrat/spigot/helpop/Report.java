package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report {

    private Player player;
    private String message;
    private ConfigData config = ConfigData.getInstance();

    public Report(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public void sendReport(Boolean feedback) {
        ArrayList<Player> admins = getAdministration();
        if(admins.size() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("no_admins")));
        } else {
            for (Player admin : admins) {
                admin.sendMessage(customizeChatMessage());
                if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                    admin.sendTitle(customizeTitleMessage(), customizeSubtitleMessage());
                }
            }
            if (feedback) player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("feedback")));
            if (config.isDatabaseEnabled()) saveReport();
        }
    }

    private void saveReport() {
        Database.getInstance()
                .update("INSERT INTO `"+config.getDatabaseParams("table")+"` " +
                "VALUES (NULL, '"+player.getName()+"', '"+player.getUniqueId()+"', '"+message+"', '-1', "+
                (Database.getInstance().getType().equals("MYSQL")?"NOW()":"DATETIME()")+
                ");");
    }



    private String customizeChatMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("admin_message_format").replace("<message>", message).replace("<player>", player.getName()));
    }
    private String customizeTitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("screen_title").replace("<message>", message).replace("<player>", player.getName()));
    }

    private String customizeSubtitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("screen_subtitle").replace("<message>", message).replace("<player>", player.getName()));
    }

    public static ArrayList<Player> getAdministration() {
        ConfigData config = ConfigData.getInstance();
        ArrayList<Player> admins = new ArrayList<>();
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer.hasPermission(config.getPerms("receive"))) admins.add(loopPlayer);
        }
        return admins;
    }
}
