package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report extends RawReport {

    public Report(Player player, String message) {
        super(player, message);
    }

    public void sendReport(Boolean feedback, Boolean saveInDB) {
        ConfigData config = ConfigData.getInstance();
        FormatMessages formater = FormatMessages.getInstance();
        ArrayList<Player> admins = getAdministration();
        for (Player admin : admins) {
            admin.sendMessage(customizeChatMessage());
            if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                admin.sendTitle(customizeTitleMessage(), customizeSubtitleMessage());
                setAnyAdminGot(true);
            }
        }
        if (config.isBungeeEnabled()) sendToBungee();
        if (config.isSendingWithoutAdmin() || isAnyAdminGot()) {
            if (feedback) Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.feedback"));
            if (config.isDatabaseEnabled() && saveInDB) saveReport();
        } else {
            if (config.isBungeeEnabled()) {
                HelpOPTB.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                    if (isAnyAdminGot()) {
                        if (feedback) Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.feedback"));
                        if (config.isDatabaseEnabled() && saveInDB) saveReport();
                    } else {
                        Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.no_admins"));
                    }
                }, 3);
            } else {
                Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.no_admins"));
            }
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
