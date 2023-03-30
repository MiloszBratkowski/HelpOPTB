package pl.techbrat.spigot.helpop.bungeecord;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.ArrayList;

public class BungeeStaffVanish implements Listener {
    private static BungeeStaffVanish instance;
    private ArrayList<String> staffQueue = new ArrayList<>();

    public BungeeStaffVanish() {
        instance = this;
    }
/*
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (ConfigData.getInstance().isBungeeAutoVanish()) {
            Player staff = event.getPlayer();
            if (staffQueue.contains(staff.getName()+staff.getUniqueId().toString())) {

                staff.hidePlayer(HelpOPTB.getInstance(), staff);
            }
        }
    }
*/
    public static BungeeStaffVanish getInstance() {
        return instance;
    }
}
