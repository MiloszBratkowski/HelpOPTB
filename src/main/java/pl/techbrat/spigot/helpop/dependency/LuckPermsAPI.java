package pl.techbrat.spigot.helpop.dependency;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.Objects;

public class LuckPermsAPI {
    private LuckPerms luckPerms;

    public LuckPermsAPI() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            HelpOPTB.getInstance().getLogger().info("LuckPerms placeholders hooked!");
            luckPerms = provider.getProvider();
        }
    }

    public String getPrefix(String player) {
        try {
            CachedMetaData data = luckPerms.
                    getUserManager().
                    getUser(player).
                    getCachedData().
                    getMetaData();
            if (data.getPrefix() == null) return "";
            else return data.getPrefix();
        } catch (Exception e){
            return "";
        }

    }

    public String getSuffix(String player) {
        try {
            CachedMetaData data = luckPerms.
                    getUserManager().
                    getUser(player).
                    getCachedData().
                    getMetaData();
            if (data.getSuffix() == null) return "";
            else return data.getSuffix();
        } catch (Exception e){
            return "";
        }
    }


}
