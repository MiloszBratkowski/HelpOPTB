package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.techbrat.spigot.helpop.API.HelpOPTBAPI;
import pl.techbrat.spigot.helpop.commands.HelpOPCommand;

import java.util.logging.Level;

public final class HelpOPTB extends JavaPlugin implements PluginMessageListener {
    private static HelpOPTB instance;
    public static HelpOPTB getInstance() {
        return instance;
    }

    private int mainIntVersion;

    @Override
    public void onEnable() {
        instance = this;
        mainIntVersion = Integer.parseInt(getServer().getBukkitVersion().split("\\.")[1].split("-")[0]);
        new ConfigData();
        new Functions();
        new DatabaseReportManager();
        new HelpOPTBAPI();
        getCommand("helpop").setExecutor(new HelpOPCommand());

        if(ConfigData.getInstance().isDatabaseEnabled()) {
            Database.load();
        }

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "Stopping plugin...");

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public void stopPlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public int getVersionSymbol() {
        return mainIntVersion;
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        getLogger().info("odebrano");

        /*
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Message")) {
            for (Player p :
                    Bukkit.getOnlinePlayers()) {
                p.sendMessage(in.readUTF());
            }
        }
        */
    }
}
