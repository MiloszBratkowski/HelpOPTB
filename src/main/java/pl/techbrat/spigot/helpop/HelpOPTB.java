package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class HelpOPTB extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Starting plugin...");
        getConfig().options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO, "Stopping plugin...");
    }
}
