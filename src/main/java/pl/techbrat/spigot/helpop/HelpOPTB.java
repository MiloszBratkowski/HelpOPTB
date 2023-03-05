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

public final class HelpOPTB extends JavaPlugin {
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
        if (ConfigData.getInstance().isBungeeEnabled()) {
            Functions.getInstance().registerBungeeChannel();
        }
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Stopping plugin...");
        Functions.getInstance().unregisterBungeeChannel();
    }

    public void stopPlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public int getVersionSymbol() {
        return mainIntVersion;
    }
}
