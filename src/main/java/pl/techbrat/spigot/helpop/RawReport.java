package pl.techbrat.spigot.helpop;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RawReport {
    private final String message;
    protected final OfflinePlayer player;

    private final String date;

    private final ConfigData config = ConfigData.getInstance();

    private String solved;


    public RawReport(OfflinePlayer player, String message) {
        this(player, message, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "-1");
    }
    public RawReport(OfflinePlayer player, String message, String date, String solved) {
        this.message = message;
        this.player = player;
        this.date = date;
        this.solved = solved;
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

    public String getSolved() {
        return solved;
    }

    public String getMessage() {
        return message;
    }


    void saveReport() {
        Database.getInstance()
                .update("INSERT INTO `"+config.getDatabaseParams("table")+"` " +
                        "VALUES (NULL, '"+player.getName()+"', '"+player.getUniqueId()+"', '"+message+"', '-1', "+date+");");
    }
}
