package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BungeeServerNameDownloader implements Listener {

    private static String serverName;

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (serverName == null) {
            HelpOPTB.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                downloadName(event.getPlayer());
            }, 4);
        }
    }

    public static void setServerName(String name) {
        serverName = name;
    }

    public static String getServerName() {
        if (ConfigData.getInstance().isBungeeEnabled() && serverName != null) return serverName;
        else return Bukkit.getServerName();
    }

    public static void downloadName(Player player) {
        if (serverName == null) {
            ByteArrayDataOutput packet = ByteStreams.newDataOutput();
            packet.writeUTF("GetServer");
            player.sendPluginMessage(HelpOPTB.getInstance(), "BungeeCord", packet.toByteArray());
        }
    }
}
