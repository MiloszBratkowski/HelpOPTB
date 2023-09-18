package pl.techbrat.spigot.helpop.dependency;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.techbrat.spigot.helpop.HelpOPTB;

public class APILoader {
    private static APILoader instance;

    private LuckPermsAPI luckPermsAPI;
    private PlaceholderAPI placeholderAPI;

    public APILoader() {
        instance = this;
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            HelpOPTB.getInstance().getLogger().info("LuckPerms has founded!");
            luckPermsAPI = new LuckPermsAPI();
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            HelpOPTB.getInstance().getLogger().info("PlaceholderAPI has founded!");
            placeholderAPI = new PlaceholderAPI();
            placeholderAPI.register();
        }
    }

    public boolean isLuckPermsAPIEnabled() {
        if (luckPermsAPI == null) return false;
        else return true;
    }

    public LuckPermsAPI getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public boolean isPlacehoderAPIEnabled() {
        if (placeholderAPI == null) return false;
        else return true;
    }

    public PlaceholderAPI getPlaceholderAPI() {
        return placeholderAPI;
    }

    public static APILoader getInstance() {
        return instance;
    }
}
