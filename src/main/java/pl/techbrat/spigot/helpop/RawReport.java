package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.bungeecord.BungeeServerNameDownloader;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RawReport {

    private final int localId;
    private int id;
    private final String message;
    private final String uuid;
    private final String playerName;
    private final String date;

    private String playerLpPrefix;
    private String playerLpSuffix;
    private String playerDisplayName;

    private final String serverName;
    private final String bungeeServerName;
    private String solved;

    private String solverLpPrefix;
    private String solverLpSuffix;
    private String solverDisplayName;

    private boolean anyAdminGot;

    private final ConfigData config = ConfigData.getInstance();


    private final static HashMap<Integer, RawReport> localReports = new HashMap<>();

    protected RawReport(Player player, String message) {
        this(player.getUniqueId().toString(),
                player.getName(),
                message,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "-1",
                ConfigData.getInstance().getServerNameDeclaration(),
                BungeeServerNameDownloader.getServerName(),
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(player.getUniqueId().toString(), player.getName()):"",
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(player.getUniqueId().toString(), player.getName()):"",
                player.getDisplayName(),
                "", "", "");
    }
    public RawReport(String uuid, String playerName, String message, String date, String solved, String serverName, String bungeeServerName, String playerLpPrefix, String playerLpSuffix, String playerDisplayName, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.solved = solved;
        this.serverName = serverName; //Always set to value in config
        this.bungeeServerName = bungeeServerName; //If bungee enabled set to BungeeCord server name, else set to config value
        this.localId = localReports.keySet().size()+1;
        localReports.put(localId, this);
        this.playerLpPrefix = playerLpPrefix;
        this.playerLpSuffix = playerLpSuffix;
        this.playerDisplayName = playerDisplayName;
        this.solverLpPrefix = solverLpPrefix;
        this.solverLpSuffix = solverLpSuffix;
        this.solverDisplayName = solverDisplayName;
        this.anyAdminGot = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String customizeChatMessage() {
        return FormatMessages.getInstance().getReportFormat(getServerName(), playerName, message, "report_format", playerLpPrefix, playerLpSuffix, playerDisplayName);
    }

    public TextComponent customizeChatMessageWithHovers() {
        return FormatMessages.getInstance().getReportFormatWithHovers(String.valueOf(localId), getServerName(), getBungeeServerName(), playerName, message, "report_format", playerLpPrefix, playerLpSuffix, playerDisplayName);
    }
    public String customizeTitleMessage() {
        return FormatMessages.getInstance().getReportFormat(getServerName(), playerName, message, "screen_title", playerLpPrefix, playerLpSuffix, playerDisplayName);
    }

    public String customizeSubtitleMessage() {
        return FormatMessages.getInstance().getReportFormat(getServerName(), playerName, message, "screen_subtitle", playerLpPrefix, playerLpSuffix, playerDisplayName);
    }

    protected int getId() {
        return id;
    }

    public int getLocalId() {
        return localId;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public String getPlayerName() {
        return playerName;
    }

    protected String getSolved() {
        return solved;
    }
    public boolean isSolved() {
        return !solved.equals("-1");
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getServerName() {
        if (serverName.equals("BUNGEE") && bungeeServerName != null) {
            return bungeeServerName;
        } else {
            return serverName;
        }
    }

    public String getBungeeServerName() {
        return bungeeServerName;
    }

    public String getPlayerLpPrefix() {
        return playerLpPrefix;
    }

    public String getPlayerLpSuffix() {
        return playerLpSuffix;
    }

    public String getPlayerDisplayName() {
        return playerDisplayName;
    }

    public String getSolverLpPrefix() {
        return solverLpPrefix;
    }

    public String getSolverLpSuffix() {
        return solverLpSuffix;
    }

    public String getSolverDisplayName() {
        return solverDisplayName;
    }

    public void setAnyAdminGot(boolean anyAdminGot) {
        this.anyAdminGot = anyAdminGot;
    }

    public boolean isAnyAdminGot() {
        return anyAdminGot;
    }

    void saveReport() {
        Database.getInstance()
                .update("INSERT INTO `"+config.getDatabaseParams("table")+"` " +
                        "VALUES (NULL, '"+playerName+"', '"+uuid+"', '"+message+"', '-1', '"+date+"', '"+getServerName()+"', '"+playerLpPrefix+"', '"+playerLpSuffix+"', '"+playerDisplayName+"', '"+solverLpPrefix+"', '"+solverLpSuffix+"', '"+solverDisplayName+"');");
        try {
            ResultSet result = Database.getInstance().execute("SELECT id FROM " + config.getDatabaseParams("table") + " WHERE date = '" + date + "' AND message = '" + message + "';");
            result.next();
            id = result.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solveReport(Player solver) {
        Database.getInstance().update("UPDATE "+config.getDatabaseParams("table")+
                " SET solved = '" + solver.getName() + "',"+
                " solver_prefix = '"+(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(solver.getUniqueId().toString(), solver.getName()):"")+"',"+
                " solver_suffix = '"+(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(solver.getUniqueId().toString(), solver.getName()):"")+"',"+
                " solver_display_name = '"+solver.getDisplayName()+"'"+
                " WHERE id = " + id + ";");
    }

    void sendToBungee() {
        ByteArrayDataOutput packet = ByteStreams.newDataOutput();
        packet.writeUTF("helpoptb");
        packet.writeUTF(HelpOPTB.getInstance().getServer().getIp()+":"+HelpOPTB.getInstance().getServer().getPort());
        packet.writeUTF("helpop");
        packet.writeUTF(Integer.toString(localId));
        packet.writeUTF(message);
        packet.writeUTF(uuid);
        packet.writeUTF(playerName);
        packet.writeUTF(date);
        packet.writeUTF(solved);
        packet.writeUTF(getServerName()); //Returns value in config "server_name: VALUE" or if exists and set returns bungee
        packet.writeUTF(getBungeeServerName()); //Returns always BungeeCord serverName
        packet.writeUTF(playerLpPrefix);
        packet.writeUTF(playerLpSuffix);
        packet.writeUTF(playerDisplayName);
        packet.writeUTF(solverLpPrefix);
        packet.writeUTF(solverLpSuffix);
        packet.writeUTF(solverDisplayName);
        getPlayer().sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", packet.toByteArray());
    }

    public void sendStaffNotification() {
        ConfigData config = ConfigData.getInstance();
        ArrayList<Player> admins = getAdministration();

        for (Player admin : admins) {
            String chatMessage = customizeChatMessage();
            if (HelpOPTB.getInstance().getVersionSymbol() >= 12) {
                admin.spigot().sendMessage(customizeChatMessageWithHovers());
            } else {
                admin.sendMessage(chatMessage);
            }
            if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                admin.sendTitle(customizeTitleMessage(), customizeSubtitleMessage());
                setAnyAdminGot(true);
            }
        }
    }


    public static RawReport getLocalReport(int id) {
        return localReports.get(id);
    }

    public static ArrayList<Player> getAdministration() {
        ConfigData config = ConfigData.getInstance();
        ArrayList<Player> admins = new ArrayList<>();
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (loopPlayer.hasPermission(config.getPerms("receive"))) admins.add(loopPlayer);
        }
        return admins;
    }
}
