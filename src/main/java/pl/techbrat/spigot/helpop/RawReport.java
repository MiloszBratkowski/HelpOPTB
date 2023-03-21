package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.bungeecord.BungeeServerNameDownloader;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class RawReport {

    private final int localId;
    private int id;
    private final String message;
    private final String uuid;
    private final String playerName;
    private final String date;

    private String lpPrefix;
    private String lpSuffix;

    private final String serverName;
    private String solved;

    private final ConfigData config = ConfigData.getInstance();


    private final static HashMap<Integer, RawReport> localReports = new HashMap<>();

    protected RawReport(Player player, String message) {
        this(player.getUniqueId().toString(),
                player.getName(),
                message,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "-1",
                BungeeServerNameDownloader.getServerName(),
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(player.getName()):"",
                APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(player.getName()):"");
    }
    public RawReport(String uuid, String playerName, String message, String date, String solved, String serverName, String lpPrefix, String lpSuffix) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.solved = solved;
        this.serverName = serverName;
        this.localId = localReports.keySet().size()+1;
        localReports.put(localId, this);
        this.lpPrefix = lpPrefix;
        this.lpSuffix = lpSuffix;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String customizeChatMessage() {
        return FormatMessages.getInstance().getReportFormat(serverName, playerName, message, "report_format", lpPrefix, lpSuffix);
    }
    public String customizeTitleMessage() {
        return FormatMessages.getInstance().getReportFormat(serverName, playerName, message, "screen_title", lpPrefix, lpSuffix);
    }

    public String customizeSubtitleMessage() {
        return FormatMessages.getInstance().getReportFormat(serverName, playerName, message, "screen_subtitle", lpPrefix, lpSuffix);
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
        return serverName;
    }

    public String getLpPrefix() {
        return lpPrefix;
    }

    public String getLpSuffix() {
        return lpSuffix;
    }

    void saveReport() {
        Database.getInstance()
                .update("INSERT INTO `"+config.getDatabaseParams("table")+"` " +
                        "VALUES (NULL, '"+playerName+"', '"+uuid+"', '"+message+"', '-1', '"+date+"', '"+serverName+"');");
        try {
            ResultSet result = Database.getInstance().execute("SELECT id FROM " + config.getDatabaseParams("table") + " WHERE date = '" + date + "' AND message = '" + message + "';");
            result.next();
            id = result.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solveReport(String admin) {
        solved = admin;
        Database.getInstance().update("UPDATE "+config.getDatabaseParams("table")+" SET solved = '"+solved+"' WHERE id = "+id+";");
    }

    void sendToBungee() {
        ByteArrayDataOutput packet = ByteStreams.newDataOutput();
        packet.writeUTF("helpoptb");
        packet.writeUTF(HelpOPTB.getInstance().getServer().getIp()+":"+HelpOPTB.getInstance().getServer().getPort());
        packet.writeUTF("helpop");
        packet.writeUTF(Integer.toString(id));
        packet.writeUTF(message);
        packet.writeUTF(uuid);
        packet.writeUTF(playerName);
        packet.writeUTF(date);
        packet.writeUTF(solved);
        packet.writeUTF(BungeeServerNameDownloader.getServerName());
        packet.writeUTF(lpPrefix);
        packet.writeUTF(lpSuffix);
        getPlayer().sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", packet.toByteArray());
    }


    public static RawReport getLocalReport(int id) {
        return localReports.get(id);
    }
}
