package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.techbrat.spigot.helpop.FormatMessages;
import pl.techbrat.spigot.helpop.HelpOPTB;
import pl.techbrat.spigot.helpop.PlayerData;
import pl.techbrat.spigot.helpop.RawReport;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.util.HashMap;

public class BungeeStaffInfo implements Listener {
    private static BungeeStaffInfo instance;
    private HashMap<String, String> staffBackServer = new HashMap<>();
    private HashMap<String, Integer> staffReportId = new HashMap<>();

    public BungeeStaffInfo() {
        instance = this;
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        if (hasStaffBackServer(event.getPlayer())) {
            removeStaffBackServer(event.getPlayer());
            removeStaffReportId(event.getPlayer());
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player staff = event.getPlayer();
        if (hasStaffBackServer(staff)) {
            FormatMessages formatMessages = FormatMessages.getInstance();
            RawReport report = RawReport.getLocalReport(getStaffReportId(staff, true));
            event.getPlayer().sendMessage(formatMessages.getMovedYourself(report.getServerName(), report.getPlayerName(), report.getPlayerLpPrefix(), report.getPlayerLpSuffix(), report.getPlayerDisplayName()));

            String staffLpPrefix;
            String staffLpSuffix;
            APILoader apiLoader = APILoader.getInstance();
            if (apiLoader.isLuckPermsAPIEnabled()) {
                staffLpPrefix = apiLoader.getLuckPermsAPI().getPrefix(staff.getUniqueId().toString(), staff.getName());
                staffLpSuffix = apiLoader.getLuckPermsAPI().getSuffix(staff.getUniqueId().toString(), staff.getName());
            } else {
                staffLpSuffix = "";
                staffLpPrefix = "";
            }

            for (Player admin : RawReport.getAdministration()) {
                if (admin != staff) {
                    admin.sendMessage(formatMessages.getMovedAdmin(report.getServerName(), report.getPlayerName(), report.getPlayerLpPrefix(), report.getPlayerLpSuffix(), report.getPlayerDisplayName(), staff.getName(), staffLpPrefix, staffLpSuffix, staff.getDisplayName()));
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                ByteArrayDataOutput infoPacket = ByteStreams.newDataOutput();
                infoPacket.writeUTF("helpoptb");
                infoPacket.writeUTF(HelpOPTB.getInstance().getServer().getIp()+":"+HelpOPTB.getInstance().getServer().getPort());
                infoPacket.writeUTF("adminMovedInfo");
                infoPacket.writeUTF(report.getServerName());
                infoPacket.writeUTF(report.getPlayerName());
                infoPacket.writeUTF(report.getPlayerLpPrefix());
                infoPacket.writeUTF(report.getPlayerLpSuffix());
                infoPacket.writeUTF(report.getPlayerDisplayName());
                infoPacket.writeUTF(staff.getName());
                infoPacket.writeUTF(staffLpPrefix);
                infoPacket.writeUTF(staffLpSuffix);
                infoPacket.writeUTF(staff.getDisplayName());
                staff.sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", infoPacket.toByteArray());
            }, 5);
        }
    }

    public void setStaffBackServer(String playerData, String bungeeServer) {
        staffBackServer.put(playerData, bungeeServer);
    }

    public String getStaffBackServer(Player player, boolean remove) {
        return getStaffBackServer(player.getName()+player.getUniqueId().toString(), remove);
    }

    public String getStaffBackServer(String playerData, boolean remove) {
        String backServer = staffBackServer.get(playerData);
        if (remove) removeStaffBackServer(playerData);
        return backServer;
    }

    private void removeStaffBackServer(Player player) {
        removeStaffBackServer(player.getName()+player.getUniqueId().toString());
    }

    private void removeStaffBackServer(String playerData) {
        staffBackServer.remove(playerData);
    }

    public boolean hasStaffBackServer(Player player) {
        return hasStaffBackServer(player.getName()+player.getUniqueId().toString());
    }

    public boolean hasStaffBackServer(String playerData) {
        return staffBackServer.containsKey(playerData);
    }


    public void setStaffReportId(String playerData, Integer reportID) {
        staffReportId.put(playerData, reportID);
    }

    public Integer getStaffReportId(Player player, boolean remove) {
        return getStaffReportId(player.getName()+player.getUniqueId().toString(), remove);
    }
    public Integer getStaffReportId(String playerData, boolean remove) {
        Integer reportId = staffReportId.get(playerData);
        if (remove) removeStaffReportId(playerData);
        return reportId;
    }

    private void removeStaffReportId(Player player) {
        removeStaffReportId(player.getName()+player.getUniqueId().toString());
    }

    private void removeStaffReportId(String playerData) {
        staffReportId.remove(playerData);
    }

    public static BungeeStaffInfo getInstance() {
        return instance;
    }
}
