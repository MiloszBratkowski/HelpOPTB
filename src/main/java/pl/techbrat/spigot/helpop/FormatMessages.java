package pl.techbrat.spigot.helpop;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.bungeecord.BungeeServerNameDownloader;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.util.ArrayList;
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

    public String removeAllColors(String message) {
        System.out.println(" ");
        System.out.println("Wiad: " +message);
        String newMessage = "";
        if(message.contains("&")) {
            for (String word : message.split("&")) {
                System.out.println("Word: "+word);
                if (word.startsWith("#")) {
                    newMessage+=word.substring(7);
                    System.out.println("form 7");
                }
                else if (word.length() > 0){
                    System.out.println("form 1");
                    newMessage+=word.substring(1);
                }
                org.bukkit.ChatColor.
                System.out.println(newMessage);
                System.out.println();
            }

        } else newMessage = message;
        System.out.println("Gotowa: "+newMessage);
        return newMessage;
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
                replace("<move_button>", getMoveButton(server)).
                replace("<response_button>", getResponseButton(player, lpPrefix, lpSuffix, displayName)).
                replace("<player_display_name>", displayName)));
    }

    public String getReportDiscordFormat(String type, String server, String player, String message, String lpPrefix, String lpSuffix, String displayName, String date) {
        return removeAllColors(replacePrefix(configData.getMsg("discord."+type).
                replace("<message>", message).
                replace("<player>", player).
                replace("<server>", server).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix).
                replace("<date>", date).
                replace("<player_display_name>", displayName)));
    }

    public TextComponent getReportFormatWithHovers(String reportId, String server, String bungeeServer, String player, String message, String type, String lpPrefix, String lpSuffix, String displayName) {
        TextComponent movebutton = new TextComponent(getMoveButton(server));
        if (bungeeServer.equals(BungeeServerNameDownloader.getServerName())) {
            movebutton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getBungeeHoverCurrent(server)).create()));
        } else {
            movebutton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getBungeeHoverSend(server)).create()));
            movebutton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helpop move " + reportId));
        }

        TextComponent responseButton = new TextComponent(getResponseButton(player, lpPrefix, lpSuffix, displayName));
        responseButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getResponseHover(player, lpPrefix, lpSuffix, displayName)).create()));
        responseButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/response "+player+" "));

        ArrayList<TextComponent> textElements = new ArrayList<>();
        String basic = configData.getMsg("admins.reports."+type).
                replace("<message>", message).
                replace("<player>", player).
                replace("<server>", server).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix).
                replace("<player_display_name>", displayName);
        String[] splitMoveButton = basic.split("<move_button>");
        for (int i = 0; i < splitMoveButton.length; i++) {
            String[] splitResponseButton = splitMoveButton[i].split("<response_button>");
            for (int j = 0; j < splitResponseButton.length; j++) {
                textElements.add(new TextComponent(addColors(splitResponseButton[j])));
                if (j != splitResponseButton.length-1) {
                    textElements.add(responseButton);
                }
            }
            if (i != splitMoveButton.length-1) {
                textElements.add(movebutton);
            }
        }
        TextComponent mainText = new TextComponent("");
        for (TextComponent component : textElements) {
            mainText.addExtra(component);
        }

        return mainText;
    }

    public String getMoveButton(String server) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.move_button").
                replace("<server>", server)));
    }

    public String getResponseButton(String player, String lpPrefix, String lpSuffix, String displayName) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.response_button").
                replace("<player>", player).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix).
                replace("<player_display_name>", displayName)));
    }

    public String getBungeeHoverSend(String server) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.bungee_send").
                replace("<server>", server)));
    }

    public String getBungeeHoverCurrent(String server) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.bungee_current").
                replace("<server>", server)));
    }

    public String getResponseHover(String player, String lpPrefix, String lpSuffix, String displayName) {
        return addColors(replacePrefix(configData.getMsg("admins.reports.response_info").
                replace("<player>", player).
                replace("<lp_player_prefix>", lpPrefix).
                replace("<lp_player_suffix>", lpSuffix).
                replace("<player_display_name>", displayName)));
    }

    public String getResponseOfflinePlayer(String player) {
        return addColors(replacePrefix(configData.getMsg("admins.commands.response.offline_player").
                replace("<player>", player)));
    }

    public String getEnabledNotifying() {
        return addColors(replacePrefix(configData.getMsg("admins.commands.notify.enabled")));
    }
    public String getDisabledNotifying() {
        return addColors(replacePrefix(configData.getMsg("admins.commands.notify.disabled")));
    }


    public static FormatMessages getInstance() {
        return instance;
    }
}
