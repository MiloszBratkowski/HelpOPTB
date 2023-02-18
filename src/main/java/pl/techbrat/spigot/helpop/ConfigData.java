package pl.techbrat.spigot.helpop;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigData {
    private static ConfigData instance;

    public static ConfigData getInstance() {
        return instance;
    }

    private final HashMap<String, String> perms = new HashMap<>();
    private final HashMap<String, String> infos = new HashMap<>();
    private final HashMap<String, String> styles = new HashMap<>();

    private final boolean screenEnabled;
    private final boolean databaseEnabled;

    private final HashMap<String, String> databaseParams = new HashMap<>();


    public ConfigData() {
        instance = this;

        FileConfiguration config = HelpOPTB.getInstance().getConfig();
        perms.put("report", "helpoptb.report"); //+
        perms.put("receive", "helpoptb.receive"); //+
        perms.put("receive.screen", "helpoptb.receive.screen"); //++
        perms.put("check", "helpoptb.check");
        perms.put("history", "helpoptb.history");
        perms.put("reload", "helpoptb.reload");

        infos.put("no_permission_player", config.getString("no_permission_player"));
        infos.put("no_permission_admin", config.getString("no_permission_admin"));
        infos.put("incorrect_use", config.getString("incorrect_use")); //+
        infos.put("no_admins", config.getString("no_admins")); //+
        infos.put("feedback", config.getString("feedback")); //+

        styles.put("admin_message_format", config.getString("admin_message_format")); //+
        styles.put("screen_title", config.getString("screen_title")); //+
        styles.put("screen_subtitle", config.getString("screen_subtitle")); //+

        screenEnabled = config.getBoolean("screen_information"); //+
        databaseEnabled = config.getBoolean("enable_history"); //+

        databaseParams.put("type", config.getString("database.type"));
        databaseParams.put("table", config.getString("database.table"));
        databaseParams.put("filename", config.getString("filename.type"));
        databaseParams.put("host", config.getString("host.type"));
        databaseParams.put("port", config.getString("port.type"));
        databaseParams.put("database", config.getString("database.type"));
        databaseParams.put("username", config.getString("username.type"));
        databaseParams.put("password", config.getString("password.type"));
        databaseParams.put("ssl", config.getString("ssl.type"));
    }

    public String getDatabaseParams(String value) {
        return databaseParams.get(value);
    }

    public String getStyles(String value) {
        return styles.get(value);
    }

    public String getInfos(String value) {
        return infos.get(value);
    }

    public String getPerms(String value) {
        return perms.get(value);
    }

    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }

    public boolean isScreenEnabled() {
        return screenEnabled;
    }


}
