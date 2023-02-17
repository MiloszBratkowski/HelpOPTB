package pl.techbrat.spigot.helpop;

import org.bukkit.plugin.java.JavaPlugin;
import pl.techbrat.spigot.helpop.commands.HelpOPCommand;

import java.util.logging.Level;

public final class HelpOPTB extends JavaPlugin {

    private static HelpOPTB instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().log(Level.INFO, "Starting plugin...");
        saveDefaultConfig();
        new ConfigData();
        getCommand("helpop").setExecutor(new HelpOPCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "Stopping plugin...");
    }

    public static HelpOPTB getInstance() {
        return instance;
    }
}
