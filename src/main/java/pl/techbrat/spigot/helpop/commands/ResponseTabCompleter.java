package pl.techbrat.spigot.helpop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import pl.techbrat.spigot.helpop.ConfigData;

import java.util.ArrayList;
import java.util.List;

public class ResponseTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (sender.hasPermission(ConfigData.getInstance().getPerms("help"))) {
            if (args.length == 2) {
                return result;
            }
        }
        return null;
    }
}
