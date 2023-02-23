package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Functions {
    private static Functions instance;
    public static Functions getInstance() {
        return instance;
    }

    private static final int LIMIT = 5;

    public Functions() {
        instance = this;
    }

    public void displayHistory(CommandSender sender, int type, Integer page) {
        ConfigData configData = ConfigData.getInstance();
        String title = configData.getInfos("history").replace("<page>", Integer.toString(page)).replace("<all_pages>", Integer.toString((int)Math.ceil(getNumberOfReports(0)*1.0/LIMIT))).replace("<amount>", Integer.toString(getNumberOfReports(0)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
        for (RawReport report:reportsFromDatabase(0, (page-1)*LIMIT, LIMIT)) {
            sender.sendMessage(report.getMessage()+" "+report.getSolved());
        }
    }

    public void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHelpOP&2TB &7all commands:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop check &7- marking reports as solved,"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d/helpop history <site> &7- displaying history of reports,"));
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

    private ArrayList<RawReport> reportsFromDatabase(int type, int first, int limit) {
        ArrayList<RawReport> reports = new ArrayList<>();
        String query = "SELECT player_name, player_uuid, message, solved, date FROM "+Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+" ORDER BY date DESC LIMIT "+first+", "+limit+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            while (result.next()) {
                reports.add(new RawReport(Bukkit.getOfflinePlayer(result.getString("player_uuid")), result.getString("message"), result.getString("date"), result.getString("solved")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }


}
