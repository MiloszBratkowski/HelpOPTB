package pl.techbrat.spigot.helpopnew.reports;

import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.bungeecord.BungeeServerNameDownloader;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportManager {
    private ArrayList<Report> reports = new ArrayList<>();

    public Report getReport(int id) {
        return reports.get(id);
    }

    public int getId(Report report) {
        return reports.indexOf(report);
    }

    public Report createReport(Player player, String message) {
        return createReport(
                player.getUniqueId().toString(),
                player.getName(),
                message,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "-1",
                ConfigData.getInstance().getServerNameDeclaration(),
                BungeeServerNameDownloader.getServerName(),
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(player.getUniqueId().toString(), player.getName()):"",
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(player.getUniqueId().toString(), player.getName()):"",
                player.getDisplayName(),
                "",
                "",
                ""
        );
    }

    public Report createReport(String uuid, String playerName, String message, String date, String solved, String serverName, String bungeeServerName, String playerLpPrefix, String playerLpSuffix, String playerDisplayName, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        Report report = new Report(uuid, playerName, message, date, solved, serverName, bungeeServerName, playerLpPrefix, playerLpSuffix, playerDisplayName, solverLpPrefix, solverLpSuffix, solverDisplayName);
        reports.add(report);
        return report;
    }
}
