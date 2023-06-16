package pl.techbrat.spigot.helpop.bungeecord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.logging.Level;

public class BungeeLoader {
    private static final HelpOPTB plugin = HelpOPTB.getInstance();
    private static BungeeLoader instance;

    public BungeeLoader() {
        this(false);
    }

    public BungeeLoader(boolean registerChannel) {
        if (registerChannel) {
            if (getInstance() != null) getInstance().unregisterBungeeChannel();
            registerBungeeChannel();
        }
        instance = this;
    }

    public void registerBungeeChannel() {
        plugin.getLogger().log(Level.INFO, "Registering BungeeCord connection...");
        HelpOPTB plugin = HelpOPTB.getInstance();
        BungeeReceiver receiver = new BungeeReceiver();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "techbrat:channel");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "techbrat:channel", receiver);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", receiver);
        plugin.getServer().getPluginManager().registerEvents(new BungeeServerNameDownloader(), plugin);
        for (Player p : Bukkit.getOnlinePlayers()) {
            BungeeServerNameDownloader.downloadName(p);
            break;
        }
        new BungeePlayerListDownloader();
        new BungeeStaffInfo();
        plugin.getLogger().log(Level.INFO, "BungeeCord registered.");
    }

    public void unregisterBungeeChannel() {
        HelpOPTB plugin = HelpOPTB.getInstance();
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "techbrat:channel");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "techbrat:channel");
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
        HandlerList.unregisterAll(BungeeServerNameDownloader.getInstance());
    }

    public static BungeeLoader getInstance() {
        return instance;
    }
}
