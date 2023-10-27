package pl.techbrat.spigot.helpopnew;

import org.bukkit.plugin.java.JavaPlugin;
import pl.techbrat.spigot.helpopnew.configuration.ConfigData;
import pl.techbrat.spigot.helpopnew.reports.ReportManager;

public class HelpOPTB extends JavaPlugin {
    private static HelpOPTB instance;
    public static HelpOPTB getInstance() {
        return instance;
    }

    private short version;

    private ConfigData configData;
    private ReportManager reportManager;

    @Override
    public void onEnable() {
        instance = this;
        version = Short.parseShort(getServer().getBukkitVersion().split("\\.")[1].split("-")[0]);

        reload();
    }

    private void reload() {
        configData = new ConfigData();
        reportManager = new ReportManager();

    }

    public void stopPlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }
}
