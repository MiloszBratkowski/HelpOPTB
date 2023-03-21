package pl.techbrat.spigot.helpop.dependency;

import org.bukkit.Bukkit;
import pl.techbrat.spigot.helpop.HelpOPTB;

public class APILoader {
    private static APILoader instance;

    private LuckPermsAPI luckPermsAPI;

    public APILoader() {
        instance = this;
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            HelpOPTB.getInstance().getLogger().info("LuckPerms has founded!");
            luckPermsAPI = new LuckPermsAPI();
        }
    }

    public boolean isLuckPermsAPIEnabled() {
        if (luckPermsAPI == null) return false;
        else return true;
    }

    public LuckPermsAPI getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public static APILoader getInstance() {
        return instance;
    }
}
