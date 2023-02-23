package pl.techbrat.spigot.helpop;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class RawReport {

    private int id;
    private final String message;
    protected final OfflinePlayer player;
    private final String playerName;
    private final String date;
    private String solved;

    private final ConfigData config = ConfigData.getInstance();




    public RawReport(Player player, String message) {
        this(player, player.getName(), message, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "-1");
    }
    public RawReport(OfflinePlayer player, String playerName, String message, String date, String solved) {
        this.player = player;
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.solved = solved;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected String customizeChatMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("admin_message_format").replace("<message>", message).replace("<player>", player.getName()));
    }
    protected String customizeTitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("screen_title").replace("<message>", message).replace("<player>", player.getName()));
    }

    protected String customizeSubtitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getStyles("screen_subtitle").replace("<message>", message).replace("<player>", player.getName()));
    }

    public int getId() {
        return id;
    }

    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getSolved() {
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


    void saveReport() {
        Database.getInstance()
                .update("INSERT INTO `"+config.getDatabaseParams("table")+"` " +
                        "VALUES (NULL, '"+player.getName()+"', '"+player.getUniqueId()+"', '"+message+"', '-1', '"+date+"');");
        try {
            ResultSet result = Database.getInstance().execute("SELECT id FROM " + config.getDatabaseParams("table") + " WHERE date = '" + date + "' AND message = '" + message + "';");
            result.next();
            id = result.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solveReport(Player admin) {
        Database.getInstance().update("UPDATE "+config.getDatabaseParams("table")+" SET solved = '"+admin.getName()+"' WHERE id = "+id+";");
    }
}
