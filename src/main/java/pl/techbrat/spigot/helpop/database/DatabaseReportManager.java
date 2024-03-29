package pl.techbrat.spigot.helpop.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.RawReport;
import pl.techbrat.spigot.helpop.dependency.APILoader;

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

    //types: 0 - all, 1 - unsolved, 2 - solved
    public Collection<RawReport> reportsFromDatabase(int type, int first, int limit) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        LinkedHashMap<Integer, RawReport> prototype = new LinkedHashMap<>();
        String query = "SELECT * FROM "+ Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+" ORDER BY date DESC LIMIT "+first+", "+limit+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            RawReport report;
            while (result.next()) {
                report = new RawReport(result.getString("player_uuid"), result.getString("player_name"), result.getString("message"), result.getString("date"), result.getString("solved"), result.getString("server"), result.getString("server"), result.getString("player_prefix"), result.getString("player_suffix"), result.getString("player_display_name"), result.getString("solver_prefix"), result.getString("solver_suffix"), result.getString("solver_display_name"));
                report.setId(result.getInt("id"));
                prototype.put(result.getInt("id"), report);
            }
            reports.putAll(prototype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prototype.values();
    }

    protected RawReport reportFromDataBase(int id) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        RawReport report = null;
        String query = "SELECT * FROM "+Database.getInstance().getTable()+" WHERE id = "+id+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            if (result.next()) {
                report = new RawReport(result.getString("player_uuid"), result.getString("player_name"), result.getString("message"), result.getString("date"), result.getString("solved"), result.getString("server"), result.getString("server"), result.getString("player_prefix"), result.getString("player_suffix"), result.getString("player_display_name"), result.getString("solver_prefix"), result.getString("solver_suffix"), result.getString("solver_display_name"));
                report.setId(result.getInt("id"));
                reports.put(id, report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

    public boolean softSolve(int id, Player solver) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        if (containsId(id)) {
            getReport(id).solveReport(solver);
            return true;
        }
        try {
            if (Database.getInstance().execute("SELECT id FROM " + ConfigData.getInstance().getDatabaseParams("table") + " WHERE id = " + id + ";").next()) {
                Database.getInstance().update("UPDATE " + ConfigData.getInstance().getDatabaseParams("table") +
                        " SET solved = '" + solver.getName() + "',"+
                        " solver_prefix = '"+(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(solver.getUniqueId().toString(), solver.getName()):"")+"',"+
                        " solver_suffix = '"+(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(solver.getUniqueId().toString(), solver.getName()):"")+"',"+
                        " solver_display_name = '"+solver.getDisplayName()+"'"+
                        " WHERE id = " + id + ";");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public RawReport getReport(int id) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        if (reports.containsKey(id)) return reports.get(id);
        else {
            return reportFromDataBase(id);
        }
    }

    public boolean containsId(int id) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        if (reports.containsKey(id)) return true;
        else {
            reportFromDataBase(id);
            return reports.containsKey(id);
        }

    }

    protected Collection<RawReport> getReports() throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
        return reports.values();
    }

    //types: 0 - all, 1 - unsolved, 2 - solved
    public void clearReports(int type) throws DatabaseDisabledException {
        if (!ConfigData.getInstance().isDatabaseEnabled()) {
            throw new DatabaseDisabledException("Database (history of reports) is disabled!");
        }
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
