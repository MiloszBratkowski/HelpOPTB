package pl.techbrat.spigot.helpop;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

public class ConfigData {
    private static final HelpOPTB plugin = HelpOPTB.getInstance();

    private static ConfigData instance;
    public static ConfigData getInstance() {
        return instance;
    }

    private final HashMap<String, String> perms = new HashMap<>();
    private HashMap<String, Object> messages = new HashMap<>();

    private HashMap<String, Object> config = new HashMap<>();


    public ConfigData() {
        instance = this;

        createConfigs(false);

        plugin.getLogger().log(Level.INFO, "Loading config file...");

        perms.put("report", "helpoptb.report"); //+
        perms.put("receive", "helpoptb.receive"); //+
        perms.put("receive.screen", "helpoptb.receive.screen"); //+
        perms.put("check", "helpoptb.command.check");
        perms.put("history", "helpoptb.command.history"); //+
        perms.put("clear.all", "helpoptb.command.clear.all");
        perms.put("clear.solved", "helpoptb.command.clear.solved");
        perms.put("reload", "helpoptb.command.reload");
        perms.put("help", "helpoptb.command.help"); //+

        File messagesFile = new File(plugin.getDataFolder()+"/messages.yml");
        messages = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(messagesFile).getConfigurationSection("").getValues(true);
        messages.put("disabled_database", "&cTo use history of reports you have to set enable_history: true in config.yml.");

        File configFile = new File(plugin.getDataFolder()+"/config.yml");
        config = (HashMap<String, Object>) YamlConfiguration.loadConfiguration(configFile).getConfigurationSection("").getValues(true);

        plugin.getLogger().log(Level.INFO, "Config file loaded.");
    }

    protected String getDatabaseParams(String value) {
        return config.get("database."+value).toString();
    }

    public String getMsg(String value) {
        return (value.contains("admins.commands")?"&7[&2HelpOP&bTB&7] ":"")+messages.get(value).toString().replace("<prefix>", messages.get("prefix").toString());
    }

    public String getPerms(String value) {
        return perms.get(value);
    }

    public boolean isDatabaseEnabled() {
        return (boolean) config.get("enable_history");
    }

    protected boolean isScreenEnabled() {
        return (boolean) config.get("screen_information");
    }

    protected boolean isSendingWithoutAdmin() {
        return (boolean) config.get("send_without_admin");
    }

    private void createConfigs(boolean forceCopy) {
        String[] files = {"config.yml", "messages.yml"};
        for (String element : files) {
            File file = new File(plugin.getDataFolder()+"/"+element);
            if (!file.isFile() || forceCopy) {
                try {
                    plugin.getLogger().log(Level.INFO, "Creating "+element+" ...");
                    FileUtils.copyInputStreamToFile(plugin.getResource(element), file);
                    plugin.getLogger().log(Level.INFO, "File "+element+" created");
                } catch (Exception e) {e.printStackTrace(); plugin.stopPlugin();}
            }
        }
    }

}
