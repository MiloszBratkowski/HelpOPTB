package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.database.DatabaseDisabledException;
import pl.techbrat.spigot.helpop.database.DatabaseReportManager;
import pl.techbrat.spigot.helpop.dependency.APILoader;

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


}
