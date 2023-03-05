package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report extends RawReport {

    public Report(Player player, String message) {
        super(player, message);
    }

    public void sendReport(Boolean feedback, Boolean saveInDB) {
        ConfigData config = ConfigData.getInstance();
        ArrayList<Player> admins = getAdministration();
        if(admins.size() == 0 && !config.isSendingWithoutAdmin()) {
            Bukkit.getPlayer(getPlayerName()).sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("players.no_admins")));
            return;
        }
        for (Player admin : admins) {
            admin.sendMessage(customizeChatMessage());
            if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                admin.sendTitle(customizeTitleMessage(), customizeSubtitleMessage());
            }
        }
        if (feedback) Bukkit.getPlayer(getPlayerName()).sendMessage(ChatColor.translateAlternateColorCodes('&', config.getMsg("players.feedback")));
        if (config.isBungeeEnabled()) sendToBungee();
        if (config.isDatabaseEnabled() && saveInDB) saveReport();
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
