package pl.techbrat.spigot.helpopnew.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.techbrat.spigot.helpopnew.HelpOPTB;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class ConfigData {
    private static final HelpOPTB plugin = HelpOPTB.getInstance();

    private final HashMap<String, String> perms = new HashMap<>();

    private final HashMap<String, Object> defaultMessages;
    private final HashMap<String, Object> messages;

    private final HashMap<String, Object> defaultConfig;
    private final HashMap<String, Object> config;

    private HashMap<String, Object> cooldownGroups = new HashMap<>();

    public ConfigData() {

        plugin.getDataFolder().mkdir();

        createConfigs(false);

        plugin.getLogger().log(Level.INFO, "Loading config file...");

        perms.put("report", "helpoptb.report"); //+

        perms.put("receive", "helpoptb.receive"); //+
        perms.put("receive.screen", "helpoptb.receive.screen"); //+
        perms.put("move", "helpoptb.move"); //+

        perms.put("response", "helpoptb.command.response"); //+
        perms.put("notify", "helpoptb.command.notify"); //+
        perms.put("check", "helpoptb.command.check");
        perms.put("history", "helpoptb.command.history"); //+
        perms.put("clear.all", "helpoptb.command.clear.all"); //+
        perms.put("clear.solved", "helpoptb.command.clear.solved"); //+
        perms.put("help", "helpoptb.command.help"); //+
        perms.put("reload", "helpoptb.command.reload"); //+
        perms.put("info", "helpoptb.command.info"); //+
        perms.put("update", "helpoptb.command.update"); //+

        File messagesFile = new File(plugin.getDataFolder()+ "/messages.yml");
        messages = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(messagesFile).getConfigurationSection("").getValues(true);
        messages.put("disabled_database", "&cTo use history of reports you have to set enable_history: true in config.yml.");
        messages.put("disabled_bungee", "&cTo use that feature you have to set enable_bungee: true in config.yml");
        File temp = new File(plugin.getDataFolder()+"/messages.temp");
        try {
            Files.copy(plugin.getResource("messages.yml"), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {e.printStackTrace();}
        defaultMessages = (HashMap<String, Object>)YamlConfiguration.loadConfiguration(temp).getConfigurationSection("").getValues(true);
        temp.delete();

        File configFile = new File(plugin.getDataFolder()+"/config.yml");
        config = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(configFile).getConfigurationSection("").getValues(true);
        temp = new File(plugin.getDataFolder()+"/config.temp");
        try {
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

    // Config settings getters

    // screen_information
    public boolean isScreenEnabled() {
        return ((boolean) getReliabilityConfig("screen_information")) && !Bukkit.getBukkitVersion().contains("1.8");
    }


    // send_without_admin
    public boolean isSendingWithoutAdmin() {
        return (boolean) getReliabilityConfig("send_without_admin");
    }


    /*
        cooldown:
            normal:
            ...:
     */
    public double getCooldown(String group) {
        if (cooldownGroups.containsKey(group)) return Double.parseDouble(cooldownGroups.get(group).toString());
        else return 0.0;
    }

    public Set<String> getCooldownGroups() {
        return cooldownGroups.keySet();
    }


    // enable_history
    public boolean isHistoryEnabled() {
        return (boolean) getReliabilityConfig("enable_history");
    }


    /*
        database:
            type:
            table:
            ...
     */
    public String getDatabaseParams(String value) {
        return getReliabilityConfig("database."+value).toString();
    }


    // enable_global
    public boolean isGlobalEnabled() {
        return (boolean) getReliabilityConfig("enable_global");
    }


    // global.type
    public String getGlobalType() {
        return getReliabilityConfig("global.type").toString();
    }


    // global.server_name
    public String getGlobalServerName() {
        return getReliabilityConfig("global.server_name").toString();
    }


    // global.receive_player_nickname_format
    public boolean isGlobalReceivePlayerNickname() {
        return (boolean) getReliabilityConfig("global.receive_player_nickname_format");
    }


    // global.receive_admin_nickname_format
    public boolean isGlobalReceiveAdminNickname() {
        return (boolean) getReliabilityConfig("global.receive_admin_nickname_format");
    }


    // global.global_network_servers
    public String[] getGlobalNetworkServers() {
        return (String[]) getReliabilityConfig("global.global_network_servers");
    }


    // enable_discord
    public boolean isDiscordEnabled() {
        return ((boolean) getReliabilityConfig("enable_discord"));
    }


    // discord.webhook_url
    public String getDiscordWebhook() {
        return (String) getReliabilityConfig("discord.webhook_url");
    }


    // discord.sender_avatar
    public boolean isDiscordPlayerAvatar() {
        return ((boolean) getReliabilityConfig("discord.sender_avatar"));
    }


    public String getMsg(String value) {
        return getReliabilityMessage(value).toString().replace("\"", "").replace("<prefix>", getReliabilityMessage("prefix").toString());
    }

    public String getPerms(String value) {
        return perms.get(value);
    }

    private void createConfigs(boolean forceCopy) {
        String[] files = {"config.yml", "messages.yml"};
        for (String element : files) {
            File file = new File(plugin.getDataFolder()+"/"+element);
            if (!file.isFile() || forceCopy) {
                try {
                    plugin.getLogger().log(Level.INFO, "Creating "+element+" ...");
                    Files.copy(plugin.getResource(element), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().log(Level.INFO, "File "+element+" created");
                } catch (Exception e) {e.printStackTrace(); plugin.stopPlugin();}
            }
        }
    }
}
