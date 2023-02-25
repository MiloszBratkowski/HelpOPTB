package pl.techbrat.spigot.helpop;

import org.bukkit.plugin.java.JavaPlugin;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "Stopping plugin...");
    }

    public void stopPlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public int getVersionSymbol() {
        return mainIntVersion;
    }
}
