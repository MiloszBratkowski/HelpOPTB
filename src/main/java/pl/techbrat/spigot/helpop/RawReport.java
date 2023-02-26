package pl.techbrat.spigot.helpop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RawReport {

    private int id;
    private final String message;
    private final String uuid;
    private final String playerName;
    private final String date;
    private String solved;

    private final ConfigData config = ConfigData.getInstance();



    protected RawReport(Player player, String message) {
        this(player.getUniqueId().toString(), player.getName(), message, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "-1");
    }
    protected RawReport(String uuid, String playerName, String message, String date, String solved) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.message = message;
        this.date = date;
        this.solved = solved;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String customizeChatMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.report_format").replace("<message>", message).replace("<player>", playerName));
    }
    protected String customizeTitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.screen_title").replace("<message>", message).replace("<player>", playerName));
    }

    protected String customizeSubtitleMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getMsg("admins.reports.screen_subtitle").replace("<message>", message).replace("<player>", playerName));
    }

    protected int getId() {
        return id;
    }

    /*protected OfflinePlayer getOfflinePlayer() {
        return player;
    }*/

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
                        "VALUES (NULL, '"+playerName+"', '"+uuid+"', '"+message+"', '-1', '"+date+"');");
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
}
