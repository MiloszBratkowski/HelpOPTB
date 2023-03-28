package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        }
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

            String normalMessage = report.customizeChatMessage();
            if (HelpOPTB.getInstance().getVersionSymbol() >= 12) {
                TextComponent chatMessage = new TextComponent(normalMessage);
                chatMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(FormatMessages.getInstance().getBungeeSend(bungeeServerName)).create()));
                chatMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helpop move " + report.getLocalId()));
                for (Player admin : admins) {
                    if (admin.hasPermission(config.getPerms("move"))) admin.spigot().sendMessage(chatMessage);
                    else admin.sendMessage(normalMessage);
                    if (config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                        admin.sendTitle(report.customizeTitleMessage(), report.customizeSubtitleMessage());
                    }
                }
            } else {
                for (Player admin : admins) {
                    admin.sendMessage(normalMessage);
                    if (config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                        admin.sendTitle(report.customizeTitleMessage(), report.customizeSubtitleMessage());
                    }
                }
            }
        }
    }
}
