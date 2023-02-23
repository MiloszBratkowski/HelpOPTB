package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report extends RawReport {
    private ConfigData config = ConfigData.getInstance();

    public Report(Player player, String message) {
        super(player, message);
    }

    public void sendReport(Boolean feedback) {
        ArrayList<Player> admins = getAdministration();
        if(admins.size() == 0) {
            getOfflinePlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("no_admins")));
        } else {
            for (Player admin : admins) {
                admin.sendMessage(customizeChatMessage());
                if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                    admin.sendTitle(customizeTitleMessage(), customizeSubtitleMessage());
                }
            }
            if (feedback) getOfflinePlayer().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getInfos("feedback")));
            if (config.isDatabaseEnabled()) saveReport();
        }
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
