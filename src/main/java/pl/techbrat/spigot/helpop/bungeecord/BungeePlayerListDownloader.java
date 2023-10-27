package pl.techbrat.spigot.helpop.bungeecord;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeePlayerListDownloader {

    private static BungeePlayerListDownloader instance;
    private ArrayList<String> players;

    public BungeePlayerListDownloader() {
        instance = this;
        players = new ArrayList<>();
    }

    public void setPlayers(String[] players) {
        this.players.clear();
        this.players.addAll(Arrays.asList(players));
    }
    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void downloadPlayers(Player player) {
        ByteArrayDataOutput packet = ByteStreams.newDataOutput();
        packet.writeUTF("PlayerList");
        packet.writeUTF("ALL");
        player.sendPluginMessage(HelpOPTB.getInstance(), "BungeeCord", packet.toByteArray());
    }

    public static BungeePlayerListDownloader getInstance() {
        return instance;
    }
}
