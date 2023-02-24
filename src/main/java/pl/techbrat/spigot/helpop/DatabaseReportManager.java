package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.util.*;

public class DatabaseReportManager {
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

    //types: 0 - all, 1 - unsolved, 2 - solved
    protected Collection<RawReport> reportsFromDatabase(int type, int first, int limit) {
        LinkedHashMap<Integer, RawReport> prototype = new LinkedHashMap<>();
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

    public boolean containsId(int id) {
        return reports.containsKey(id);
    }

    public Collection<RawReport> getReports() {
        return reports.values();
    }

    //types: 0 - all, 1 - unsolved, 2 - solved
    public void clearReports(int type) {
        Database.getInstance().update("DELETE FROM "+ConfigData.getInstance().getDatabaseParams("table")+" "+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+";");
        if (type==0) reports.clear();
        else {
            for(Iterator<Map.Entry<Integer, RawReport>> it = reports.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, RawReport> entry = it.next();
                if(type==1 && !reports.get(entry.getKey()).isSolved()) it.remove();
                else if(type==2 && reports.get(entry.getKey()).isSolved()) it.remove();
            }
        }

    }
}
