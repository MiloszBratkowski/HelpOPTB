package pl.techbrat.spigot.helpop.bungeecord;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class BungeeStaffInfo {
    private static BungeeStaffInfo instance;
    private HashMap<String, String> staffBackServer = new HashMap<>();

    public BungeeStaffInfo() {
        instance = this;
    }

    public void setStaffBackServer(String playerData, String bungeeServer) {
        staffBackServer.put(playerData, bungeeServer);
    }

    public String getStaffBackServer(Player player, boolean remove) {
        return getStaffBackServer(player.getName()+player.getUniqueId().toString(), remove);
    }

    public String getStaffBackServer(String playerData, boolean remove) {
        String backServer = staffBackServer.get(playerData);
        if (remove) staffBackServer.remove(playerData);
        return backServer;
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
