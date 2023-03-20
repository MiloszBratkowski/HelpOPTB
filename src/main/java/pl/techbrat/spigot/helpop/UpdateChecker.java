package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class UpdateChecker implements Listener {
    private static UpdateChecker instance;

    private String currentVersion;
    private String latestVersion;

    public UpdateChecker(boolean checkUpdate) {
        instance = this;
        HelpOPTB plugin = HelpOPTB.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        if (checkUpdate) checkUpdate(true, true);
    }
    
    public void stopListening() {
        HandlerList.unregisterAll(this);
    }

    public ArrayList<String> getUpdateMessage(boolean colors) {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.add((colors?"&7[&dHelpOP&bTB&7] &a":"")+"New update available!");
        list.add((colors?"&7[&dHelpOP&bTB&7] &6":"")+"To best performance plugin should be updated!");
        list.add((colors?"&7[&dHelpOP&bTB&7] &7":"")+"This version: "+(colors?"&c":"")+currentVersion+(colors?"&7":"")+". Latest stable version: "+(colors?"&a":"")+latestVersion);
        list.add((colors?"&7[&dHelpOP&bTB&7] &6":"")+"Download from: https://www.spigotmc.org/resources/helpoptb.108278/");
        list.add("");
        return list;
    }

    public void checkUpdate(boolean sendConsole, boolean sendAdmin) {
        HelpOPTB plugin = HelpOPTB.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=108278").openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    latestVersion = scanner.next();
                    currentVersion = plugin.getDescription().getVersion();
                    if (sendConsole) sendInfo(Bukkit.getConsoleSender());
                    if (sendAdmin) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission(ConfigData.getInstance().getPerms("update"))) {
                                sendInfo(player);
                            }
                        }
                    }
                }
            } catch (IOException exception) {
                plugin.getLogger().severe("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    public void sendInfo(CommandSender sender) {
        if (!latestVersion.equalsIgnoreCase(currentVersion)) {
            HelpOPTB plugin = HelpOPTB.getInstance();
            if (sender instanceof Player) {
                for (String line : getUpdateMessage(true)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
            } else {
                for (String line : getUpdateMessage(false)) {
                    plugin.getLogger().warning(line);
                }
            }
        }
    }

    public void sendLatest(CommandSender sender) {
        if (latestVersion.equalsIgnoreCase(currentVersion)) {
            HelpOPTB plugin = HelpOPTB.getInstance();
            sender.sendMessage("");
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dHelpOP&bTB&7] &aYou have latest version! &7(&a"+currentVersion+"&7)"));
            } else {
                plugin.getLogger().info("You have latest version! ("+currentVersion+")");
            }
            sender.sendMessage("");
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission(ConfigData.getInstance().getPerms("update"))) {
            sendInfo(event.getPlayer());
        }
    }

    public static UpdateChecker getInstance() {
        return instance;
    }
}
