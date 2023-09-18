package pl.techbrat.spigot.helpop.dependency;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.techbrat.spigot.helpop.*;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return true;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "helpoptb";
    }

    @Override
    public @NotNull String getAuthor() {
        return HelpOPTB.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return HelpOPTB.getInstance().getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("notify_status")) {
            FormatMessages formatMessages = FormatMessages.getInstance();
            if (PlayerData.getInstance().hasDisabledNotify(player)) return formatMessages.formatMessage("placeholderapi.notify_status.disabled");
            else return formatMessages.formatMessage("placeholderapi.notify_status.enabled");
        } /*else if (params.equals("admin_status")) {
            FormatMessages formatMessages = FormatMessages.getInstance();
            if (RawReport.getAdministration().size() > 0) return formatMessages.formatMessage("placeholderapi.admin_status.available");
            else return formatMessages.formatMessage("placeholderapi.admin_status.unavailable");
        }*/
        return null;
    }
}
