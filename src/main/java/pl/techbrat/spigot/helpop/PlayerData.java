package pl.techbrat.spigot.helpop;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerData {
    private static PlayerData instance;

    private final ArrayList<String> playersInQueue = new ArrayList<>();

    private final ArrayList<String> notifyDisabledAdmins = new ArrayList<>();


    public PlayerData() {
        instance = this;
    }

    public void setTimer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!playersInQueue.contains(uuid)) {
            playersInQueue.add(uuid);
            double time = 0.0;
            for (String group : ConfigData.getInstance().getCooldownGroups()) {
                if (player.hasPermission("helpoptb.cooldown." + group)) {
                    time = ConfigData.getInstance().getCooldown(group);
                    break;
                }
            }
            HelpOPTB.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HelpOPTB.getInstance(), () -> {
                playersInQueue.remove(uuid);
            }, Math.round(time * 20));
        }
    }

    public boolean canSend(Player player) {
        return !playersInQueue.contains(player.getUniqueId().toString());
    }

    public boolean changeNotify(Player player) {
        if (hasDisabledNotify(player)) enableNotify(player);
        else disableNotify(player);
        return !hasDisabledNotify(player);
    }

    public void disableNotify(Player player) {
        notifyDisabledAdmins.add(player.getUniqueId().toString());
    }

    public void enableNotify(Player player) {
        notifyDisabledAdmins.remove(player.getUniqueId().toString());
    }

    public boolean hasDisabledNotify(Player player) {
        return notifyDisabledAdmins.contains(player.getUniqueId().toString());
    }

    public static PlayerData getInstance() {
        return instance;
    }


}
