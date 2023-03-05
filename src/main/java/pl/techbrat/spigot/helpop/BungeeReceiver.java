package pl.techbrat.spigot.helpop;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;

public class BungeeReceiver implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        HelpOPTB plugin = HelpOPTB.getInstance();
        if (!channel.equals("techbrat:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        if (!in.readUTF().equals("helpoptb") || in.readUTF().equals(plugin.getServer().getIp()+":"+plugin.getServer().getPort())) {
            return;
        }
        int id = Integer.parseInt(in.readUTF());
        String mess = in.readUTF();
        String uuid = in.readUTF();
        String playerName = in.readUTF();
        String date = in.readUTF();
        String solved = in.readUTF();
        RawReport report = new RawReport(uuid, playerName, mess, date, solved);

        ConfigData config = ConfigData.getInstance();
        ArrayList<Player> admins = Report.getAdministration();
        for (Player admin : admins) {
            admin.sendMessage(report.customizeChatMessage());
            if(config.isScreenEnabled() && admin.hasPermission(config.getPerms("receive.screen"))) {
                admin.sendTitle(report.customizeTitleMessage(), report.customizeSubtitleMessage());
            }
        }
    }
}
