package pl.techbrat.spigot.helpop;

import org.bukkit.plugin.java.JavaPlugin;
import pl.techbrat.spigot.helpop.API.HelpOPTBAPI;
import pl.techbrat.spigot.helpop.bungeecord.BungeeLoader;
import pl.techbrat.spigot.helpop.commands.HelpOPCommand;
import pl.techbrat.spigot.helpop.commands.HelpOPTabCompleter;
import pl.techbrat.spigot.helpop.commands.ResponseCommand;
import pl.techbrat.spigot.helpop.commands.ResponseTabCompleter;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.database.DatabaseReportManager;
import pl.techbrat.spigot.helpop.dependency.APILoader;

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

        new UpdateChecker(true);

        new ConfigData();
        new Functions();
        new DatabaseReportManager();
        new HelpOPTBAPI();
        new PlayerData();
        new FormatMessages();

        new APILoader();


        getCommand("helpop").setExecutor(new HelpOPCommand());
        getCommand("response").setExecutor(new ResponseCommand());
        getCommand("helpop").setTabCompleter(new HelpOPTabCompleter());
        getCommand("response").setTabCompleter(new ResponseTabCompleter());

        if(ConfigData.getInstance().isDatabaseEnabled()) {
            Database.load();
        }
        if (ConfigData.getInstance().isBungeeEnabled()) {
            new BungeeLoader(true);
        }
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Stopping plugin...");
        if (ConfigData.getInstance().isBungeeEnabled()) BungeeLoader.getInstance().unregisterBungeeChannel();
        if (ConfigData.getInstance().isDatabaseEnabled()) Database.getInstance().disconnect();
    }

    public void stopPlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public int getVersionSymbol() {
        return mainIntVersion;
    }
}
