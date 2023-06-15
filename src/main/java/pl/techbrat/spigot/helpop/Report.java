package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Report extends RawReport {

    public Report(Player player, String message) {
        super(player, message);
    }

    public void sendReport(Boolean feedback, Boolean saveInDB) {
        sendStaffNotification();

        ConfigData config = ConfigData.getInstance();
        FormatMessages formater = FormatMessages.getInstance();
        if (config.isBungeeEnabled()) sendToBungee();
        if (config.isSendingWithoutAdmin() || isAnyAdminGot()) {
            if (feedback) Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.feedback"));
            if (config.isDatabaseEnabled() && saveInDB) saveReport();
            if (config.isDiscordEnabled()) sendDiscordNotification();
        } else {
            if (config.isBungeeEnabled()) {
                HelpOPTB.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                    if (isAnyAdminGot()) {
                        if (feedback) Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.feedback"));
                        if (config.isDatabaseEnabled() && saveInDB) saveReport();
                        if (config.isDiscordEnabled()) sendDiscordNotification();
                    } else {
                        Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.no_admins"));
                    }
                }, 3);
            } else {
                Bukkit.getPlayer(getPlayerName()).sendMessage(formater.formatMessage("players.no_admins"));
            }
        }
    }
}
