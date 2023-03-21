package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.techbrat.spigot.helpop.*;

import java.util.ArrayList;

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

        ConfigData config = ConfigData.getInstance();
        if (type.equals("response")) {
            String admin = in.readUTF();
            String playerName = in.readUTF();
            String mess = in.readUTF();
            Player user = Bukkit.getPlayer(playerName);
            if (user != null && user.isOnline()) {
                Functions.getInstance().respondedInfo(admin, playerName, mess);
                user.sendMessage(FormatMessages.getInstance().getResponse(admin, playerName, mess, false));
            }
        } else if (type.equals("helpop")) {
            int id = Integer.parseInt(in.readUTF());
            String mess = in.readUTF();
            String uuid = in.readUTF();
            String playerName = in.readUTF();
            String date = in.readUTF();
            String solved = in.readUTF();
            String serverName = in.readUTF();
            String lpPrefix = in.readUTF();
            String lpSuffix = in.readUTF();
            RawReport report = new RawReport(uuid, playerName, mess, date, solved, serverName, lpPrefix, lpSuffix);

            ArrayList<Player> admins = Report.getAdministration();
            if (admins.size() > 0) {
                String normalMessage = report.customizeChatMessage();
                if (HelpOPTB.getInstance().getVersionSymbol() >= 12) {
                    TextComponent chatMessage = new TextComponent(normalMessage);
                    chatMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(FormatMessages.getInstance().getBungeeSend(serverName)).create()));
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
}
