package pl.techbrat.spigot.helpop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.Functions;

import java.util.ArrayList;
import java.util.List;

public class HelpOPTabCompleter implements TabCompleter {

    private ArrayList<String> completes = new ArrayList<>();

    public HelpOPTabCompleter() {
        completes.add("notify");
        completes.add("history");
        completes.add("check");
        completes.add("clear_all");
        completes.add("clear_solved");
        completes.add("back");
        completes.add("reload");
        completes.add("update");
        completes.add("help");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (sender.hasPermission(ConfigData.getInstance().getPerms("help"))) {
            if (args.length == 1) {
                StringUtil.copyPartialMatches(args[0], completes, result);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("history")) {
                for (int i = 1; i < Functions.getInstance().getNumbersOfPages(0); i++) {
                    result.add(Integer.toString(i));
                }
            }
        }
        return result;
    }
}
