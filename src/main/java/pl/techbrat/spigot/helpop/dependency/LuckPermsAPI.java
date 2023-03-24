package pl.techbrat.spigot.helpop.dependency;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class LuckPermsAPI {
    private LuckPerms luckPerms;

    public LuckPermsAPI() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            HelpOPTB.getInstance().getLogger().info("LuckPerms placeholders hooked!");
            luckPerms = provider.getProvider();
        }
    }

    private User getForceUser(String uuid, String player) throws ExecutionException, InterruptedException {
        if (luckPerms.getUserManager().isLoaded(UUID.fromString(uuid))) {
            Bukkit.getLogger().info("Zaladowany.");
            return luckPerms.getUserManager().getUser(player);
        } else {
            Bukkit.getLogger().info("Pobieranie.");
            return luckPerms.getUserManager().loadUser(UUID.fromString(uuid), player).get();
        }
    }

    public String getPrefix(String uuid, String player) {
        try {
            CachedMetaData data = getForceUser(uuid, player).getCachedData().getMetaData();
            if (data.getPrefix() == null) return "";
            else return data.getPrefix();
        } catch (Exception e){
            return "";
        }

    }

    public String getSuffix(String uuid, String player) {
        try {
            CachedMetaData data = getForceUser(uuid, player).getCachedData().getMetaData();
            if (data.getSuffix() == null) return "";
            else return data.getSuffix();
        } catch (Exception e){
            return "";
        }
    }


}
