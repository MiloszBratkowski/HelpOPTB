package pl.techbrat.spigot.helpop.API;

import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.DatabaseDisabledException;
import pl.techbrat.spigot.helpop.DatabaseReportManager;
import pl.techbrat.spigot.helpop.Report;

import java.util.ArrayList;

public class HelpOPTBAPI {
    private static HelpOPTBAPI instance;
    public static HelpOPTBAPI getApi() {
        return instance;
    }

    public HelpOPTBAPI() {
        instance = this;
    }

    public boolean sendReport(Player sender, String message) {
        return sendReport(sender, message, true);
    }

    public boolean sendReport(Player sender, String message, Boolean feedback) {
        if (Report.getAdministration().size() > 1) {
            Report report = new Report(sender, message);
            report.sendReport(feedback, true);
            return true;
        } else return false;
    }

    public boolean isAdministration() {
        if (Report.getAdministration().size() > 0) return true;
        else return false;
    }

    public ArrayList<Player> getAdministration() {
        return Report.getAdministration();
    }

    public boolean isDatabaseEnabled() {
        return ConfigData.getInstance().isDatabaseEnabled();
    }

    public boolean isReportAvailable(int id) throws DatabaseDisabledException {
        return DatabaseReportManager.getInstance().containsId(id);
    }

    public String getReportMessage(int id) throws DatabaseDisabledException {
        return DatabaseReportManager.getInstance().getReport(id).getMessage();
    }

    public String getReportPlayer(int id) throws DatabaseDisabledException {
        return DatabaseReportManager.getInstance().getReport(id).getPlayerName();
    }

    public String getReportDate(int id) throws DatabaseDisabledException {
        return DatabaseReportManager.getInstance().getReport(id).getDate();
    }

    public boolean isReportSolved(int id) throws DatabaseDisabledException {
        return DatabaseReportManager.getInstance().getReport(id).isSolved();
    }
}
