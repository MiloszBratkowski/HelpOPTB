package pl.techbrat.spigot.helpop.dependency;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsMeta {
    private static LuckPermsMeta instance;

    private LuckPerms luckPermsAPI;

    public LuckPermsMeta() {
        instance = this;
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsAPI = provider.getProvider();
        }
    }

    public String getPrefix(String player) {
        return luckPermsAPI.getUserManager().getUser(player).getCachedData().getMetaData().getPrefix();
    }


}
