package pl.techbrat.spigot.helpop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Functions {
    private static Functions instance;
    public static Functions getInstance() {
        return instance;
    }

    public Functions() {
        instance = this;
    }

    public void displayHistory(CommandSender sender, Integer site) {
        ConfigData configData = ConfigData.getInstance();
        String title = configData.getInfos("history").replace("<site>", site.toString()).replace("<all_sites>", "3");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
    }
}
