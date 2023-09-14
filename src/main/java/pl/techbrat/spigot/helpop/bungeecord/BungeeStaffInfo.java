package pl.techbrat.spigot.helpop.bungeecord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.HashMap;

public class BungeeStaffInfo implements Listener {
    private static BungeeStaffInfo instance;
    private HashMap<String, String> staffBackServer = new HashMap<>();

    public BungeeStaffInfo() {
        instance = this;
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        if (hasStaffBackServer(event.getPlayer())) {
            removeStaffBackServer(event.getPlayer());
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (hasStaffBackServer(event.getPlayer())) {
            event.getPlayer().sendMessage("Teleportowano na serwer ze zgloszenia");
        }
    }

    public void setStaffBackServer(String playerData, String bungeeServer) {
        staffBackServer.put(playerData, bungeeServer);
    }

    public String getStaffBackServer(Player player, boolean remove) {
        return getStaffBackServer(player.getName()+player.getUniqueId().toString(), remove);
    }

    public String getStaffBackServer(String playerData, boolean remove) {
        String backServer = staffBackServer.get(playerData);
        if (remove) removeStaffBackServer(playerData);
        return backServer;
    }

    private void removeStaffBackServer(Player player) {
        removeStaffBackServer(player.getName()+player.getUniqueId().toString());
    }

    private void removeStaffBackServer(String playerData) {
        staffBackServer.remove(playerData);
    }

    public boolean hasStaffBackServer(Player player) {
        return hasStaffBackServer(player.getName()+player.getUniqueId().toString());
    }

    public boolean hasStaffBackServer(String playerData) {
        return staffBackServer.containsKey(playerData);
    }

    public static BungeeStaffInfo getInstance() {
        return instance;
    }
}
