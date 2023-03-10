package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

    private final String serverName;
    private String solved;

    private final ConfigData config = ConfigData.getInstance();


    private final static HashMap<Integer, RawReport> localReports = new HashMap<>();

    protected RawReport(Player player, String message) {
        this(player.getUniqueId().toString(), player.getName(), message, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "-1", BungeeServerNameDownloader.getServerName());
    }
    protected RawReport(String uuid, String playerName, String message, String date, String solved, String serverName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.solved = solved;
        this.serverName = serverName;
        this.localId = localReports.keySet().size()+1;
        localReports.put(localId, this);
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String customizeChatMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.report_format").
                replace("<message>", message).
                replace("<player>", playerName).
                replace("<server>", serverName));
    }
    protected String customizeTitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.screen_title").replace("<message>", message).replace("<player>", playerName).replace("<server>", serverName));
    }

    protected String customizeSubtitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.screen_subtitle").replace("<message>", message).replace("<player>", playerName).replace("<server>", serverName));
    }

    protected int getId() {
        return id;
    }

    protected int getLocalId() {
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
        packet.writeUTF(Integer.toString(id));
        packet.writeUTF(message);
        packet.writeUTF(uuid);
        packet.writeUTF(playerName);
        packet.writeUTF(date);
        packet.writeUTF(solved);
        packet.writeUTF(BungeeServerNameDownloader.getServerName());
        getPlayer().sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", packet.toByteArray());
    }


    public static RawReport getLocalReport(int id) {
        return localReports.get(id);
    }
}
