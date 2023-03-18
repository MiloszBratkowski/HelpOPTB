package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.techbrat.spigot.helpop.API.HelpOPTBAPI;
import pl.techbrat.spigot.helpop.commands.HelpOPCommand;
import pl.techbrat.spigot.helpop.commands.HelpOPTabCompleter;
import pl.techbrat.spigot.helpop.commands.ReponseCommand;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
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

        checkUpdate();

        new ConfigData();
        new Functions();
        new DatabaseReportManager();
        new HelpOPTBAPI();
        new PlayerData();

        getCommand("helpop").setExecutor(new HelpOPCommand());
        getCommand("response").setExecutor(new ReponseCommand());
        getCommand("helpop").setTabCompleter(new HelpOPTabCompleter());

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

    public void checkUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=108278").openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String latest = scanner.next();
                    String current = getDescription().getVersion();
                    if (!latest.equalsIgnoreCase(current)) {
                        getLogger().warning("");
                        getLogger().warning("New update available!");
                        getLogger().warning("To best performance plugin should be updated!");
                        getLogger().warning("This version: "+current+". Latest stable version: "+latest);
                        getLogger().warning("Download from: https://www.spigotmc.org/resources/helpoptb.108278/");
                        getLogger().warning("");
                    }
                }
            } catch (IOException exception) {
                getLogger().severe("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
