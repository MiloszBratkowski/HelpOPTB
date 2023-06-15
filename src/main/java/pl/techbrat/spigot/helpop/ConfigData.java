package pl.techbrat.spigot.helpop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class ConfigData {
    private static final HelpOPTB plugin = HelpOPTB.getInstance();

    private static ConfigData instance;
    public static ConfigData getInstance() {
        return instance;
    }

    private final HashMap<String, String> perms = new HashMap<>();

    private final HashMap<String, Object> defaultMessages;
    private final HashMap<String, Object> messages;

    private final HashMap<String, Object> defaultConfig;
    private final HashMap<String, Object> config;

    private HashMap<String, Object> cooldownGroups = new HashMap<>();

    public ConfigData() {
        instance = this;

        plugin.getDataFolder().mkdir();

        createConfigs(false);

        plugin.getLogger().log(Level.INFO, "Loading config file...");

        perms.put("notify", "helpoptb.command.notify"); //+
        perms.put("update", "helpoptb.update"); //+
        perms.put("report", "helpoptb.report"); //+
        perms.put("receive", "helpoptb.receive"); //+
        perms.put("receive.screen", "helpoptb.receive.screen"); //+
        perms.put("move", "helpoptb.move"); //+
        perms.put("check", "helpoptb.command.check");
        perms.put("history", "helpoptb.command.history"); //+
        perms.put("clear.all", "helpoptb.command.clear.all"); //+
        perms.put("clear.solved", "helpoptb.command.clear.solved"); //+
        perms.put("reload", "helpoptb.command.reload"); //+
        perms.put("help", "helpoptb.command.help"); //+

        perms.put("response", "helpoptb.command.response"); //+

        File messagesFile = new File(plugin.getDataFolder()+"/messages.yml");
        messages = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(messagesFile).getConfigurationSection("").getValues(true);
        messages.put("disabled_database", "&cTo use history of reports you have to set enable_history: true in config.yml.");
        messages.put("disabled_bungee", "&cTo use that feature you have to set enable_bungee: true in config.yml");
        File temp = new File(plugin.getDataFolder()+"/messages.temp");
        try {
            //FileUtils.copyInputStreamToFile(plugin.getResource("messages.yml"), temp);
            //copyInputStreamToFile("src/main/resources/messages.yml", temp);
            //IOUtils.copy(plugin.getResource("messages.yml"), new FileOutputStream(temp));
            Files.copy(plugin.getResource("messages.yml"), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {e.printStackTrace();}
        defaultMessages = (HashMap<String, Object>)YamlConfiguration.loadConfiguration(temp).getConfigurationSection("").getValues(true);
        temp.delete();

        File configFile = new File(plugin.getDataFolder()+"/config.yml");
        config = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(configFile).getConfigurationSection("").getValues(true);
        temp = new File(plugin.getDataFolder()+"/config.temp");
        try {
            //FileUtils.copyInputStreamToFile(plugin.getResource("config.yml"), temp);
            //copyInputStreamToFile("src/main/resources/config.yml", temp);
            //IOUtils.copy(plugin.getResource("config.yml"), new FileOutputStream(temp));
            Files.copy(plugin.getResource("config.yml"), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {e.printStackTrace();}
        defaultConfig = (HashMap<String, Object>)YamlConfiguration.loadConfiguration(temp).getConfigurationSection("").getValues(true);
        temp.delete();

        if (YamlConfiguration.loadConfiguration(configFile).contains("cooldown")) {
            cooldownGroups = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(configFile).getConfigurationSection("cooldown").getValues(true);
        }
        plugin.getLogger().log(Level.INFO, "Config file loaded.");
    }

    private Object getReliabilityConfig(String value) {
        if (config.containsKey(value)) return config.get(value);
        else {
            plugin.getLogger().warning("");
            plugin.getLogger().warning("Can't find '"+value+"' in config.yml!");
            plugin.getLogger().warning("Default value has been got! ("+defaultConfig.get(value)+")");
            plugin.getLogger().warning("To set that option, config file must be recreated!");
            plugin.getLogger().warning("Paste manually new structures or delete config.yml to auto recreate!");
            plugin.getLogger().warning("");
            return defaultConfig.get(value);
        }
    }

    private Object getReliabilityMessage(String value) {
        if (messages.containsKey(value)) return messages.get(value);
        else {
            plugin.getLogger().warning("");
            plugin.getLogger().warning("Can't find '"+value+"' in messages.yml!");
            plugin.getLogger().warning("Default value has been got! ("+defaultMessages.get(value)+")");
            plugin.getLogger().warning("To set that option, config file must be recreated!");
            plugin.getLogger().warning("Paste manually new structures or delete messages.yml to auto recreate!");
            plugin.getLogger().warning("");
            return defaultMessages.get(value);
        }
    }

    public String getDatabaseParams(String value) {
        return getReliabilityConfig("database."+value).toString();
    }

    public String getMsg(String value) {
        return getReliabilityMessage(value).toString().replace("\"", "").replace("<prefix>", getReliabilityMessage("prefix").toString());
    }

    public String getPerms(String value) {
        return perms.get(value);
    }

    public boolean isDatabaseEnabled() {
        return (boolean) getReliabilityConfig("enable_history");
    }

    public boolean isScreenEnabled() {
        return ((boolean) getReliabilityConfig("screen_information")) && !Bukkit.getBukkitVersion().contains("1.8");
    }

    public boolean isDiscordEnabled() {
        return ((boolean) getReliabilityConfig("discord.enable"));
    }
    public String getDiscordWebhook() {
        return (String) getReliabilityConfig("discord.webhook_url");
    }
    public boolean isDiscordPlayerAvatar() {
        return ((boolean) getReliabilityConfig("discord.sender_avatar"));
    }

    public boolean isReceivedPlayerFormat() {
        return (boolean) getReliabilityConfig("receive_player_nickname_format");
    }
    public boolean isReceivedAdminFormat() {
        return (boolean) getReliabilityConfig("receive_admin_nickname_format");
    }

    public boolean isBungeeEnabled() {
        return (boolean) getReliabilityConfig("enable_bungee");
    }
/*
    public boolean isBungeeAutoVanish() {
        return (boolean) getReliabilityConfig("auto_vanish");
    }
*/
    protected boolean isSendingWithoutAdmin() {
        return (boolean) getReliabilityConfig("send_without_admin");
    }

    public String getServerNameDeclaration() {
        return (String) getReliabilityConfig("server_name");
    }

    protected double getCooldown(String group) {
        if (cooldownGroups.containsKey(group)) return Double.parseDouble(cooldownGroups.get(group).toString());
        else return 0.0;
    }
    protected Set<String> getCooldownGroups() {
        return cooldownGroups.keySet();
    }

    private void createConfigs(boolean forceCopy) {
        String[] files = {"config.yml", "messages.yml"};
        for (String element : files) {
            File file = new File(plugin.getDataFolder()+"/"+element);
            if (!file.isFile() || forceCopy) {
                try {
                    plugin.getLogger().log(Level.INFO, "Creating "+element+" ...");
                    //copyInputStreamToFile("src/main/resources/"+element, file);
                    //FileUtils.copyInputStreamToFile(plugin.getResource(element), file);
                    //IOUtils.copy(plugin.getResource(element), new FileOutputStream(file));
                    Files.copy(plugin.getResource(element), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().log(Level.INFO, "File "+element+" created");
                } catch (Exception e) {e.printStackTrace(); plugin.stopPlugin();}
            }
        }
    }

    /* OLD FUNCTION TO COPY RESOURCES TO FILES
    private void copyInputStreamToFile(String path, File file) {
        try {
            Path newPath = Paths.get(path);
            byte[] buffer = Files.readAllBytes(newPath);
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
