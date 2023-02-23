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

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
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
}
