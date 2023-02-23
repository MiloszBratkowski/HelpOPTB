package pl.techbrat.spigot.helpop;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Functions {
    private static Functions instance;
    public static Functions getInstance() {
        return instance;
    }

    private static final int LIMIT = 5; //Number of reports on the one page

    public Functions() {
        instance = this;
    }

    public void displayHistory(CommandSender sender, int type, Integer page) {
        ConfigData configData = ConfigData.getInstance();
        String title = configData.getInfos("history").replace("<page>", Integer.toString(page)).replace("<all_pages>", Integer.toString(getNumbersOfPages(0))).replace("<amount>", Integer.toString(getNumberOfReports(0)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
        for (RawReport report : DatabaseReportManager.getInstance().reportsFromDatabase(0, (page-1)*LIMIT, LIMIT)) {
            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', ConfigData.getInstance().getInfos("history_element").
                    replace("<id>", Integer.toString(report.getId())).
                    replace("<solved>", report.isSolved()?"&a✔":"&c✘").
                    replace("<solve_admin>", report.isSolved()?"&a"+report.getSolved():"&c✘").
                    replace("<date>", report.getDate()).
                    replace("<player>", report.getPlayerName()).
                    replace("<message>", report.getMessage())));
            if(report.isSolved()) {
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configData.getInfos("solve_admin").replace("<player>", report.getSolved()))).create()));
            } else {
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configData.getInfos("click_solve"))).create()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helpop check "+report.getId()));
            }
            sender.spigot().sendMessage(message);
        }
    }

    public void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHelpOP&2TB &7all commands:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop check &7- marking reports as solved,"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop history <page> &7- displaying history of reports,"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop clear_all &7- deleting all reports from database,"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop clear_solved &7- deleting solved reports from database,"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop reload &7- reloading configuration file."));
    }

    //types: 0 - all, 1 - unsolved, 2 - solved
    private int getNumberOfReports(int type) {
        String query = "SELECT COUNT(id) FROM "+Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            result.next();
            return result.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumbersOfPages(int type) {
        return (int)Math.ceil(getNumberOfReports(type)*1.0/LIMIT);
    }



    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
