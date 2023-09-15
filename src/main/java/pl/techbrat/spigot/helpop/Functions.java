package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.bungeecord.BungeeServerNameDownloader;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.database.DatabaseDisabledException;
import pl.techbrat.spigot.helpop.database.DatabaseReportManager;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;

public class Functions {
    private static Functions instance;
    public static Functions getInstance() {
        return instance;
    }

    private static final int LIMIT = 10; //Number of reports on the one page

    protected Functions() {
        instance = this;
    }

    public void displayHistory(CommandSender sender, int type, Integer page) throws DatabaseDisabledException {
        ConfigData configData = ConfigData.getInstance();
        FormatMessages formater = FormatMessages.getInstance();
        sender.sendMessage(formater.getHistoryTitle(Integer.toString(page), Integer.toString(getNumbersOfPages(type)), Integer.toString(getNumberOfReports(type))));
        for (RawReport report : DatabaseReportManager.getInstance().reportsFromDatabase(type, (page-1)*LIMIT, LIMIT)) {
            String messageOld = formater.getHistoryElement(Integer.toString(report.getId()), report.isSolved(), report.getSolved(), report.getDate(), report.getPlayerName(), report.getMessage(), report.getServerName(), report.getPlayerLpPrefix(), report.getPlayerLpSuffix(), report.getPlayerDisplayName(), report.getSolverLpPrefix(), report.getSolverLpSuffix(), report.getSolverDisplayName());
            if (HelpOPTB.getInstance().getVersionSymbol() >= 12 && sender.hasPermission(configData.getPerms("check"))) {
                TextComponent message = new TextComponent(messageOld);
                if (report.isSolved()) {
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(formater.getHistoryHoverSolve(report.getSolved(), report.getSolverLpPrefix(), report.getSolverLpSuffix(), report.getSolverDisplayName())).create()));
                } else {
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(formater.formatMessage("admins.commands.history.click_solve")).create()));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helpop check " + report.getId()));
                }
                sender.spigot().sendMessage(message);
            } else {
                sender.sendMessage(messageOld);
            }
        }
    }

    public void displayHelp(CommandSender sender) {
        sender.sendMessage(FormatMessages.getInstance().formatMessage("admins.commands.help"));
    }

    //types: 0 - all, 1 - unsolved, 2 - solved
    protected int getNumberOfReports(int type) {
        String query = "SELECT COUNT(id) FROM "+ Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            result.next();
            return result.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumbersOfPages(int type) {
        return (int)Math.ceil(getNumberOfReports(type)*1.0/LIMIT);
    }

    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public Color getColor(String colorName) {
        try {
            Field field = Class.forName("java.awt.Color").getField(colorName);
            return (Color) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }


    public Player getAnyPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            return player;
        }
        return null;
    }

    public boolean sendResponse(String playerName, String message, Player admin) {
        ConfigData config = ConfigData.getInstance();
        if (config.isBungeeEnabled()) {
            ByteArrayDataOutput packet = ByteStreams.newDataOutput();
            packet.writeUTF("helpoptb");
            packet.writeUTF(HelpOPTB.getInstance().getServer().getIp()+":"+HelpOPTB.getInstance().getServer().getPort());
            packet.writeUTF("response");
            packet.writeUTF(message);
            packet.writeUTF(admin.getName());
            packet.writeUTF(admin.getUniqueId().toString());
            packet.writeUTF(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(admin.getUniqueId().toString(), admin.getName()):"");
            packet.writeUTF(APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(admin.getUniqueId().toString(), admin.getName()):"");
            packet.writeUTF(admin.getDisplayName());
            packet.writeUTF(playerName);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", packet.toByteArray());
            }
            return true;
        } else return false;
    }

    public void respondedInfoToStaff(Player admin, String player, String message) {
        String lpAdminPrefix = APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getPrefix(admin.getUniqueId().toString(), admin.getName()):"";
        String lpAdminSuffix = APILoader.getInstance().isLuckPermsAPIEnabled()?APILoader.getInstance().getLuckPermsAPI().getSuffix(admin.getUniqueId().toString(), admin.getName()):"";
        respondedInfoToStaff(admin.getName(), player, message, lpAdminPrefix, lpAdminSuffix, admin.getDisplayName());
    }

    public void respondedInfoToStaff(String admin, String player, String message, String lpAdminPrefix, String lpAdminSuffix, String adminDisplayName) {
        ConfigData config = ConfigData.getInstance();
        String perm = config.getPerms("receive");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getName().equalsIgnoreCase(admin) && p.hasPermission(perm)) {
                p.sendMessage(FormatMessages.getInstance().getResponse(admin, player, message, lpAdminPrefix, lpAdminSuffix, adminDisplayName, true));
            }
        }
    }

    public void sendPluginConfigurationInfo(CommandSender sender) {
        FormatMessages format = FormatMessages.getInstance();
        ConfigData config = ConfigData.getInstance();
        sender.sendMessage("");
        sender.sendMessage(format.addColors("&7[&dHelpOP&bTB&7] &aInformation about plugin configuration."));
        sender.sendMessage(format.addColors("&7Displaying reports on the admin's screen (1.9+): &e"+(config.isScreenEnabled()?"YES":"NO")));
        sender.sendMessage(format.addColors("&7Sending reports with no admin on the server: &e"+(config.isSendingWithoutAdmin()?"YES":"NO")));
        sender.sendMessage(format.addColors("&7Receiving player's nickname: &e"+(config.isReceivedPlayerFormat()?"PLAYER'S":"ADMIN RECEIVER")+" SERVER"));
        sender.sendMessage(format.addColors("&7Receiving admin's nickname: &e"+(config.isReceivedAdminFormat()?"ADMIN'S":"PLAYERS RECEIVER")+" SERVER"));
        sender.sendMessage(format.addColors("&7Database configuration (history of reports):"));
        sender.sendMessage(format.addColors("&3 &7- status: &e"+(config.isDatabaseEnabled()?"ENABLED":"DISABLED")));
        sender.sendMessage(format.addColors("&3 &7- DB type: &e"+(config.isDatabaseEnabled()?config.getDatabaseParams("type"):"---")));
        sender.sendMessage(format.addColors("&3 &7- DB parameters: &e"+(
                config.isDatabaseEnabled()?
                        (config.getDatabaseParams("type").equalsIgnoreCase("mysql")?
                                        config.getDatabaseParams("host")+
                                        "&7:&e"+
                                        config.getDatabaseParams("port")+
                                        "&7, &e"+
                                        config.getDatabaseParams("database"):
                                config.getDatabaseParams("type").equalsIgnoreCase("sqlite")?
                                        config.getDatabaseParams("filename"):"WRONG")
                        :"---")));
        sender.sendMessage(format.addColors("&7BungeeCord configuration:"));
        sender.sendMessage(format.addColors("&3 &7- status: &e"+(config.isBungeeEnabled()?"ENABLED":"DISABLED")));
        sender.sendMessage(format.addColors("&3 &7- server name: &e"+(config.isDatabaseEnabled()?BungeeServerNameDownloader.getServerName().equals("BUNGEE") :"---")));
        sender.sendMessage(format.addColors("&7Discord configuration:"));
        sender.sendMessage(format.addColors("&3 &7- status: &e"+(config.isDiscordEnabled()?"ENABLED":"DISABLED")));
        sender.sendMessage(format.addColors("&3 &7- sending player's head: &e"+(config.isDiscordEnabled()?(config.isDiscordPlayerAvatar()?"YES":"NO"):"---")));
        sender.sendMessage(format.addColors("&7Founded additional plugins: &e"+(APILoader.getInstance().isLuckPermsAPIEnabled()?"LuckPerms":"---")));
    }



}
