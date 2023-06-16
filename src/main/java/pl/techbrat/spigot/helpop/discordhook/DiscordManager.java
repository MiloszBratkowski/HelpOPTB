package pl.techbrat.spigot.helpop.discordhook;

import org.bukkit.ChatColor;
import pl.techbrat.spigot.helpop.Functions;
import pl.techbrat.spigot.helpop.HelpOPTB;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class DiscordManager {

    private static final HelpOPTB plugin = HelpOPTB.getInstance();
    private static DiscordManager instance;

    private String url;

    private DiscordWebhook discordWebhook;

    public DiscordManager(String url) {
        plugin.getLogger().log(Level.INFO, "Hooking Discord...");
        instance = this;
        this.url = url;
        discordWebhook = new DiscordWebhook(url);
        plugin.getLogger().log(Level.INFO, "Discord hooked!");


        /*discordWebhook.setContent("to jest contanet");
        discordWebhook.setUsername("Nazwa ogulem cale te");
        discordWebhook.setAvatarUrl("https://premium4animals.pl/upload/premium4/blog//Kot-ragdoll.jpeg");
        EmbedObject data = new EmbedObject();
        data.setTitle("To jest title");
        discordWebhook.addEmbed(data
                .setTitle("To jest title")
                .setDescription("To jest opis")
                .setColor(Color.BLUE)
                .addField("1 pole", "Inline", true)
                .addField("2 pole", "Inline", true)
                .addField("3 pole", "No-Inline", false)
                .setThumbnail("https://kryptongta.com/images/kryptonlogo.png")
                .setFooter("Footer text", "https://kryptongta.com/images/kryptonlogodark.png")
                .setImage("https://kryptongta.com/images/kryptontitle2.png")
                .setAuthor("Author Name", "https://kryptongta.com", "https://kryptongta.com/images/kryptonlogowide.png")
                .setUrl("https://kryptongta.com")
        );

        try {
            discordWebhook.execute();
            discordWebhook.setUsername("Nazwa ogulem cale tess");
            discordWebhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void sendNotification(String name, String content, String author, String title, String footer, String avatar, String color) {
        discordWebhook = new DiscordWebhook(url);
        discordWebhook.setUsername(name);
        discordWebhook.setContent(content);
        if (avatar != null) discordWebhook.setAvatarUrl(avatar);
        discordWebhook.addEmbed(new EmbedObject()
                .setAuthor(author, null, null)
                .setColor(Functions.getInstance().getColor(color))
                .setTitle(title)
                .setFooter(footer, null)
        );
        try {
            discordWebhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiscordManager getInstance() {
        return instance;
    }
}
