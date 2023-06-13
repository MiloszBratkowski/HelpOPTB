package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
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
            receiveResponse(in.readUTF(),in.readUTF(),  in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
        } else if (type.equals("helpop")) {
            receiveHelpop(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
        } else if (type.equals("receive")) {
            RawReport.getLocalReport(Integer.parseInt(in.readUTF())).setAnyAdminGot(true);
        } else if (type.equals("backServerInfo") && in.readUTF().equals(BungeeServerNameDownloader.getServerName())) {
            setBackServer(in.readUTF(), in.readUTF());
        }
    }


    private void setBackServer(String bungeeServer, String playerData) {
        BungeeStaffInfo.getInstance().setStaffBackServer(playerData, bungeeServer);
    }

    private void receiveResponse(String message, String admin, String adminUUID, String lpAdminPrefix, String lpAdminSuffix, String adminDisplayName, String player) {
        Player user = Bukkit.getPlayer(player);
        if (user != null && user.isOnline()) {
            ConfigData config = ConfigData.getInstance();
            if (!config.isReceivedAdminFormat()) {
                APILoader apiLoader = APILoader.getInstance();
                if (apiLoader.isLuckPermsAPIEnabled()) {
                    lpAdminPrefix = apiLoader.getLuckPermsAPI().getPrefix(adminUUID, player);
                    lpAdminSuffix = apiLoader.getLuckPermsAPI().getSuffix(adminUUID, player);
                }
                if (Bukkit.getOfflinePlayer(UUID.fromString(adminUUID)).getPlayer() != null) {
                    adminDisplayName = Bukkit.getOfflinePlayer(UUID.fromString(adminUUID)).getPlayer().getDisplayName();
                }
            }
            Functions.getInstance().respondedInfoToStaff(admin, player, message, lpAdminPrefix, lpAdminSuffix, adminDisplayName);
            user.sendMessage(FormatMessages.getInstance().getResponse(admin, player, message, lpAdminPrefix, lpAdminSuffix, adminDisplayName, false));
        }
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
