package pl.techbrat.spigot.helpop.discordhook;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordManager {

    //DiscordWebhook discordWebhook;

    public DiscordManager(String url) {
        /*try {
            final HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            connection.setDoOutput(true);
            try (final OutputStream outputStream = connection.getOutputStream()) {
                // Handle backslashes.
                String preparedCommand = "sdadasdadsasd".replaceAll("\\\\", "");
                if (preparedCommand.endsWith(" *")) preparedCommand = preparedCommand.substring(0, preparedCommand.length() - 2) + "*";

                outputStream.write(("{\"content\":\"" + preparedCommand + "\"}").getBytes(StandardCharsets.UTF_8));
            }
            connection.getInputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }*/




        DiscordWebhook discordWebhook = new DiscordWebhook(url);

        /*discordWebhook.setContent("to jest contanet");
        discordWebhook.setUsername("Nazwa ogulem cale te");
        discordWebhook.setAvatarUrl("https://premium4animals.pl/upload/premium4/blog//Kot-ragdoll.jpeg");
        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject()
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
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
