package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.techbrat.spigot.helpop.*;
import pl.techbrat.spigot.helpop.dependency.APILoader;

import java.util.ArrayList;
import java.util.UUID;

public class BungeeReceiver implements PluginMessageListener {


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        HelpOPTB plugin = HelpOPTB.getInstance();
        if (!channel.equals("techbrat:channel") && !channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        if (channel.equals("BungeeCord")) {
            String type = in.readUTF();
            if (type.equals("GetServer")) {
                String name = in.readUTF();
                BungeeServerNameDownloader.setServerName(name);
            }
            else if (type.equals("PlayerList")) {
                in.readUTF();
                String[] playerList = in.readUTF().split(", ");
                BungeePlayerListDownloader.getInstance().setPlayers(playerList);
            }
            return;
        }
        if (!in.readUTF().equals("helpoptb") || in.readUTF().equals(plugin.getServer().getIp()+":"+plugin.getServer().getPort())) {
            return;
        }
        String type = in.readUTF();

        if (type.equals("response")) {
            receiveResponse(in.readUTF(),in.readUTF(),  in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
        } else if (type.equals("helpop")) {
            receiveHelpop(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
        } else if (type.equals("receive")) {
            RawReport.getLocalReport(Integer.parseInt(in.readUTF())).setAnyAdminGot(true);
        } else if (type.equals("backServerInfo") && in.readUTF().equals(BungeeServerNameDownloader.getServerName())) {
            setStaffInfo(in.readUTF(), Integer.parseInt(in.readUTF()), in.readUTF());
        } else if (type.equals("adminMovedInfo")) {
            sendAdminMovedInfo(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
        }
    }

    private void sendAdminMovedInfo(String server, String player, String lpPrefix, String lpSuffix, String displayName, String admin, String adminLpPrefix, String adminLpSuffix, String adminDisplayName) {
        for (Player staff : RawReport.getAdministration()) {
                staff.sendMessage(FormatMessages.getInstance().getMovedAdmin(server, player, lpPrefix, lpSuffix, displayName, admin, adminLpPrefix, adminLpSuffix, adminDisplayName));
        }
    }

    private void setStaffInfo(String bungeeServer, Integer reportId, String playerData) {
        BungeeStaffInfo.getInstance().setStaffBackServer(playerData, bungeeServer);
        BungeeStaffInfo.getInstance().setStaffReportId(playerData, reportId);
    }

    private void receiveResponse(String message, String player, String lpPlayerPrefix, String lpPlayerSuffix, String playerDisplayName, String admin, String lpAdminPrefix, String lpAdminSuffix, String adminDisplayName, String adminUUID) {
        Bukkit.getLogger().info("odebrano");
        OfflinePlayer user = Bukkit.getOfflinePlayer(player);
        ConfigData config = ConfigData.getInstance();
        APILoader apiLoader = APILoader.getInstance();
        if (!config.isReceivedPlayerFormat()) {
            if (user != null && user.hasPlayedBefore()) {
                if (apiLoader.isLuckPermsAPIEnabled()) {
                    lpPlayerPrefix = apiLoader.getLuckPermsAPI().getPrefix(user.getUniqueId().toString(), player);
                    lpPlayerSuffix = apiLoader.getLuckPermsAPI().getSuffix(user.getUniqueId().toString(), player);
                }
                if (user.getPlayer() != null) {
                    playerDisplayName = user.getPlayer().getDisplayName();
                }
            }
        }
        if (!config.isReceivedAdminFormat()) {
            if (apiLoader.isLuckPermsAPIEnabled()) {
                lpAdminPrefix = apiLoader.getLuckPermsAPI().getPrefix(adminUUID, player);
                lpAdminSuffix = apiLoader.getLuckPermsAPI().getSuffix(adminUUID, player);
            }
            if (Bukkit.getOfflinePlayer(UUID.fromString(adminUUID)).getPlayer() != null) {
                adminDisplayName = Bukkit.getOfflinePlayer(UUID.fromString(adminUUID)).getPlayer().getDisplayName();
            }
        }
        if (user != null && user.getPlayer() != null && user.getPlayer().isOnline()) {
            user.getPlayer().sendMessage(FormatMessages.getInstance().getResponse(message, player, lpPlayerPrefix, lpPlayerSuffix, playerDisplayName, admin, lpAdminPrefix, lpAdminSuffix, adminDisplayName, false));
        }
        Functions.getInstance().respondedInfoToStaff(message, player, lpPlayerPrefix, lpPlayerSuffix, playerDisplayName, admin, lpAdminPrefix, lpAdminSuffix, adminDisplayName);
    }

    private void receiveHelpop(String localId, String message, String uuid, String player, String date, String solved, String serverName, String bungeeServerName, String playerLpPrefix, String playerLpSuffix, String playerDisplayName, String solverLpPrefix, String solverLpSuffix, String solverDisplayName) {
        ConfigData config = ConfigData.getInstance();

        if (!config.isReceivedPlayerFormat()) {
            APILoader apiLoader = APILoader.getInstance();
            if (apiLoader.isLuckPermsAPIEnabled()) {
                playerLpPrefix = apiLoader.getLuckPermsAPI().getPrefix(uuid, player);
                playerLpSuffix = apiLoader.getLuckPermsAPI().getSuffix(uuid, player);
            }
            if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer() != null) {
                playerDisplayName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer().getDisplayName();
            }
        }
        RawReport report = new RawReport(uuid, player, message, date, solved, serverName, bungeeServerName, playerLpPrefix, playerLpSuffix, playerDisplayName, solverLpPrefix, solverLpSuffix, solverDisplayName);

        ArrayList<Player> admins = Report.getAdministration();
        if (admins.size() > 0) {
            ByteArrayDataOutput packet = ByteStreams.newDataOutput();
            packet.writeUTF("helpoptb");
            packet.writeUTF(HelpOPTB.getInstance().getServer().getIp()+":"+HelpOPTB.getInstance().getServer().getPort());
            packet.writeUTF("receive");
            packet.writeUTF(localId);
            admins.get(0).sendPluginMessage(HelpOPTB.getInstance(), "techbrat:channel", packet.toByteArray());


            report.sendStaffNotification();
        }
    }
}
