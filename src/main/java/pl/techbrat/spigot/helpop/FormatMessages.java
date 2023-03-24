package pl.techbrat.spigot.helpop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatMessages {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");;
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

    public String hexToColor(final String message) {
        final char colorChar = ChatColor.COLOR_CHAR;

        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            final String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    public String addColors(String message) {
        message = hexToColor(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String formatMessage(final String patch) {
        return addColors(replacePrefix(configData.getMsg(patch)));
    }

    public String getHistoryTitle(String page, String allPages, String amount) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.title"))
                .replace("<page>", page)
                .replace("<all_pages>", allPages)
                .replace("<amount>", amount));
    }

    public String getHistoryElement(String id, boolean solved, String solver, String date, String player, String message, String server, String playerLpPrefix, String playerLpSuffix, String playerDisplayName, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.element").
                replace("<id>", id).
                replace("<solved>", solved?"&a✔":"&c✘").
                replace("<solver>", solved?"&a"+solver:"&c✘").
                replace("<date>", date).
                replace("<player>", player).
                replace("<lp_player_prefix>", playerLpPrefix).
                replace("<lp_player_suffix>", playerLpSuffix).
                replace("<player_display_name>", playerDisplayName).
                replace("<lp_solver_prefix>", solverLpPrefix).
                replace("<lp_solver_suffix>", solverLpSuffix).
                replace("<solver_display_name>", solverDisplayName).
                replace("<server>", server).
                replace("<message>", message)));
    }

    public String getHistoryPageRage(String pageRage) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.page_rage").
                replace("<all_pages>", pageRage)));
    }
    public String getHistoryHoverSolve(String solveAdmin, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.history.hover_solve").
                replace("<lp_solver_prefix>", solverLpPrefix).
                replace("<lp_solver_suffix>", solverLpSuffix).
                replace("<solver_display_name>", solverDisplayName).
                replace("<solver>", solveAdmin)));
    }

    public String getResponse(Player admin, String player, String message, boolean forAdmin) {
        return getResponse(admin.getName(),
                player,
                message,
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(admin.getUniqueId().toString(), admin.getName()):"",
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(admin.getUniqueId().toString(), admin.getName()):"",
                admin.getDisplayName(),
                forAdmin);
    }
    public String getResponse(String admin, String player, String message, String adminPrefix, String adminSuffix, String adminDisplayName, boolean forAdmin) {
        return addColors(replacePrefix(configData.getMsg(forAdmin?"admins.commands.response.format":"players.response").
                replace("<admin>", admin).
                replace("<player>", player).
                replace("<message>", message).
                replace("<lp_admin_prefix>", adminPrefix).
                replace("<lp_admin_suffix>", adminSuffix).
                replace("<admin_display_name>", adminDisplayName)));
    }


    public String getReportFormat(String server, String player, String uuid, String message, String type) {
        APILoader apiLoader = APILoader.getInstance();
        return getReportFormat(server, player, message, type,
                apiLoader.isLuckPermsAPIEnabled()?apiLoader.getLuckPermsAPI().getPrefix(uuid, player) : "DISABLED",
                apiLoader.isLuckPermsAPIEnabled()?apiLoader.getLuckPermsAPI().getSuffix(uuid, player) : "DISABLED",
                "");
    }

    public String getReportFormat(String server, String player, String message, String type, String lpPrefix, String lpSuffix, String displayName) {
        return addColors(replacePrefix(configData.getMsg("admins.reports."+type).
                replace("<message>", message).
                replace("<player>", player).
                replace("<server>", server).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix).
                replace("<player_display_name>", displayName)));
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
