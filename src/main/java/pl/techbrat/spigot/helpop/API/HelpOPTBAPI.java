package pl.techbrat.spigot.helpop.API;

import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.Report;

import java.util.ArrayList;

public class HelpOPTBAPI {
    private static HelpOPTBAPI instance;

    public HelpOPTBAPI() {
        instance = this;
    }

    public boolean sendReport(Player sender, String message) {
        return sendReport(sender, message, true);
    }

    public boolean sendReport(Player sender, String message, Boolean feedback) {
        if (Report.getAdministration().size() > 1) {
            Report report = new Report(sender, message);
            report.sendReport(feedback);
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

    public static HelpOPTBAPI getApi() {
        return instance;
    }
}
