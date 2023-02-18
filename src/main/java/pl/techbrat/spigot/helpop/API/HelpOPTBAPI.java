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
        if (Report.getAdministration().size() > 1) {
            new Report(sender, message);
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
