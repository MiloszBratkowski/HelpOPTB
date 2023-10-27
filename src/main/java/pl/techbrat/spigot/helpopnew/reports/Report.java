package pl.techbrat.spigot.helpopnew.reports;

import org.bukkit.ChatColor;

public class Report {
    private final String uuid;
    private final String playerName;
    private final String message;
    private final String date;
    private String solved;
    private final String serverName;
    private final String bungeeServerName;
    private String playerLpPrefix;
    private String playerLpSuffix;
    private String playerDisplayName;
    private String solverLpPrefix;
    private String solverLpSuffix;
    private String solverDisplayName;

    private boolean anyAdminGot;
    public Report(String uuid, String playerName, String message, String date, String solved, String serverName, String bungeeServerName, String playerLpPrefix, String playerLpSuffix, String playerDisplayName, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.message = ChatColor.stripColor(message);
        this.date = date;
        this.solved = solved;
        this.serverName = serverName; //Always set to value in config
        this.bungeeServerName = bungeeServerName; //If bungee enabled set to BungeeCord server name, else set to config value
        this.playerLpPrefix = playerLpPrefix;
        this.playerLpSuffix = playerLpSuffix;
        this.playerDisplayName = playerDisplayName;
        this.solverLpPrefix = solverLpPrefix;
        this.solverLpSuffix = solverLpSuffix;
        this.solverDisplayName = solverDisplayName;
        this.anyAdminGot = false;
    }
}
