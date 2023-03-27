package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.HelpOPTB;

public class BungeeServerNameDownloader implements Listener {

    private static BungeeServerNameDownloader instance;

    private static String serverName;

    public BungeeServerNameDownloader() {
        instance = this;
    }

    public static BungeeServerNameDownloader getInstance() {
        return instance;
    }

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
        String first = ConfigData.getInstance().getServerNameDeclaration();
        if (ConfigData.getInstance().isBungeeEnabled() && serverName != null) return serverName;
        else return first;
    }

    public static void downloadName(Player player) {
        if (serverName == null) {
            ByteArrayDataOutput packet = ByteStreams.newDataOutput();
            packet.writeUTF("GetServer");
            player.sendPluginMessage(HelpOPTB.getInstance(), "BungeeCord", packet.toByteArray());
        }
    }
}
