package pl.techbrat.spigot.helpop;

import org.bukkit.ChatColor;
import pl.techbrat.spigot.helpop.dependency.APILoader;
import pl.techbrat.spigot.helpop.dependency.LuckPermsAPI;

public class FormatMessages {
    private static FormatMessages instance;

    private ConfigData configData;

    public FormatMessages() {
        instance = this;
        updateConfig();
    }

    public void updateConfig() {
        configData = ConfigData.getInstance();
    }

    public String getPrefix() {
        return configData.getMsg("prefix");
    }

    public String replacePrefix(String message) {
        return message.replace("<prefix>", getPrefix());
    }

    public String addColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String formatMessage(String patch) {
        return addColors(replacePrefix(configData.getMsg(patch)));
    }

    public String getHistoryTitle(String page, String allPages, String amount) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.title"))
                .replace("<page>", page)
                .replace("<all_pages>", allPages)
                .replace("<amount>", amount));
    }

    public String getHistoryElement(String id, boolean solved, String solveAdmin, String date, String player, String message, String server) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.element").
                replace("<id>", id).
                replace("<solved>", solved?"&a✔":"&c✘").
                replace("<solve_admin>", solved?"&a"+solveAdmin:"&c✘").
                replace("<date>", date).
                replace("<player>", player).
                replace("<server>", server).
                replace("<message>", message)));
    }

    public String getHistoryPageRage(String pageRage) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.page_rage").
                replace("<all_pages>", pageRage)));
    }
    public String getHistoryHoverSolve(String solveAdmin) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.hover_solve").
                replace("<solve_admin>", solveAdmin)));
    }

    public String getResponse(String admin, String player, String message, boolean forAdmin) {
        return getResponse(admin,
                player,
                message,
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(admin):"",
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(admin):"",
                forAdmin);
    }
    public String getResponse(String admin, String player, String message, String adminPrefix, String adminSuffix, boolean forAdmin) {
        return addColors(replacePrefix(configData.getMsg(forAdmin?"admins.commands.response.format":"players.response").
                replace("<admin>", admin).
                replace("<player>", player).
                replace("<message>", message).
                replace("<lp_admin_prefix>", adminPrefix).
                replace("<lp_admin_suffix>", adminSuffix)));
    }


    public String getReportFormat(String server, String player, String message, String type) {
        APILoader apiLoader = APILoader.getInstance();
        return getReportFormat(server, player, message, type,
                apiLoader.isLuckPermsAPIEnabled()?apiLoader.getLuckPermsAPI().getPrefix(player) : "DISABLED",
                apiLoader.isLuckPermsAPIEnabled()?apiLoader.getLuckPermsAPI().getSuffix(player) : "DISABLED");
    }

    public String getReportFormat(String server, String player, String message, String type, String lpPrefix, String lpSuffix) {
        return addColors(replacePrefix(configData.getMsg("admins.reports."+type).
                replace("<message>", message).
                replace("<player>", player).
                replace("<server>", server).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix)));
    }

    public String getBungeeSend(String server) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.bungee_send").
                replace("<server>", server)));
    }

    public String getResponseOfflinePlayer(String player) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.response.offline_player").
                replace("<player>", player)));
    }


    public static FormatMessages getInstance() {
        return instance;
    }
}
