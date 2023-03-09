package pl.techbrat.spigot.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Functions;

import java.util.ArrayList;
import java.util.List;

public class HelpOPTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (sender.hasPermission(ConfigData.getInstance().getPerms("help"))) {
            if (args.length == 1) {
                result.add("help");
                result.add("history");
                result.add("check");
                result.add("clear_all");
                result.add("clear_solved");
                result.add("reload");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("history")) {
                for (int i = 1; i < Functions.getInstance().getNumbersOfPages(0); i++) {
                    result.add(Integer.toString(i));
                }
            }
        }
        return result;
    }
}
