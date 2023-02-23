package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseReportManager { //TODO IN THE FUTURE
    private final HashMap<Integer, RawReport> reports = new HashMap<>();
    private static DatabaseReportManager instance;

    public DatabaseReportManager() {
        instance = this;
    }

    public static DatabaseReportManager getInstance() {
        return instance;
    }

    /*
    public void loadreports() {
        reports.clear();
        try {
            ResultSet result = Database.getInstance().execute("SELECT * FROM "+ConfigData.getInstance().getDatabaseParams("table")+";");
            while (result.next()) {
                RawReport report = new RawReport(Bukkit.getOfflinePlayer(result.getString("player_uuid")), result.getString("player_name"), result.getString("message"), result.getString("date"), result.getString("solved"));
                report.setId(result.getInt("id"));
                reports.put(result.getInt("id"), report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    protected Collection<RawReport> reportsFromDatabase(int type, int first, int limit) {
        HashMap<Integer, RawReport> prototype = new HashMap<>();
        String query = "SELECT * FROM "+Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+" ORDER BY date DESC LIMIT "+first+", "+limit+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            RawReport report;
            while (result.next()) {
                report = new RawReport(Bukkit.getOfflinePlayer(result.getString("player_uuid")), result.getString("player_name"), result.getString("message"), result.getString("date"), result.getString("solved"));
                report.setId(result.getInt("id"));
                prototype.put(result.getInt("id"), report);
            }
            reports.putAll(prototype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prototype.values();
    }

    public RawReport getReport(int id) {
        return reports.get(id);
    }

    public Collection<RawReport> getReports() {
        return reports.values();
    }
}
