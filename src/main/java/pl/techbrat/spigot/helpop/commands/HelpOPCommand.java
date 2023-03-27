package pl.techbrat.spigot.helpop.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.techbrat.spigot.helpop.*;
import pl.techbrat.spigot.helpop.API.HelpOPTBAPI;
import pl.techbrat.spigot.helpop.bungeecord.BungeeLoader;
import pl.techbrat.spigot.helpop.database.Database;
import pl.techbrat.spigot.helpop.database.DatabaseReportManager;
import pl.techbrat.spigot.helpop.dependency.APILoader;


public class HelpOPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            FormatMessages formater = FormatMessages.getInstance();
            ConfigData config = ConfigData.getInstance();
            if (args.length == 0) {
                if (sender.hasPermission(config.getPerms("report")) || sender.hasPermission("helpoptb.report")) { //TODO Remove old permission in next updates
                    sender.sendMessage(formater.formatMessage("players.incorrect_use"));
                } else {
                    sender.sendMessage(formater.formatMessage("no_permission"));
                }
                return true;
            }
            else {
                if (sender.hasPermission(config.getPerms("help")) && args[0].equals("help")) {
                    Functions.getInstance().displayHelp(sender);
                    return true;
                }
                if (sender.hasPermission(config.getPerms("history")) && args[0].equals("history")) {
                    if (!config.isDatabaseEnabled()) {
                        sender.sendMessage(formater.formatMessage("disabled_database"));
                        return true;
                    }
                    final int TYPE = 0; //TODO in the future to implement
                    int page = 1;
                    if (args.length > 1) {
                        if (Functions.getInstance().isInteger(args[1])) {
                            page = Integer.parseInt(args[1]);
                            final int all_pages = Functions.getInstance().getNumbersOfPages(TYPE);
                            if (page <= 0 || page > all_pages) {
                                sender.sendMessage(formater.getHistoryPageRage(Integer.toString(all_pages)));
                                return true;
                            }
                        }
                    }
                    Functions.getInstance().displayHistory(sender, TYPE, page);
                    return true;
                }
                if (sender.hasPermission(config.getPerms("move")) && args[0].equals("move")) {
                    if (!config.isBungeeEnabled()) {
                        sender.sendMessage(formater.formatMessage("disabled_bungee"));
                        return true;
                    }
                    if (args.length < 2 || !Functions.getInstance().isInteger(args[1])) {
                        sender.sendMessage(formater.formatMessage("admins.commands.check.type_id"));
                        return true;
                    }
                    int id = Integer.parseInt(args[1]);
                    if (RawReport.getLocalReport(id) == null) {
                        sender.sendMessage(formater.formatMessage("admins.commands.check.incorrect_id"));
                        return true;
                    }
                    ByteArrayDataOutput packet = ByteStreams.newDataOutput();
                    packet.writeUTF("Connect");
                    packet.writeUTF(RawReport.getLocalReport(id).getBungeeServerName());
                    ((Player) sender).sendPluginMessage(HelpOPTB.getInstance(), "BungeeCord", packet.toByteArray());
                    return true;
                }
                if (sender.hasPermission(config.getPerms("check")) && args[0].equals("check")) {
                    if (!config.isDatabaseEnabled()) {
                        sender.sendMessage(formater.formatMessage("disabled_database"));
                        return true;
                    }
                    if (args.length < 2 || !Functions.getInstance().isInteger(args[1])) {
                        sender.sendMessage(formater.formatMessage("admins.commands.check.type_id"));
                        return true;
                    }
                    int id = Integer.parseInt(args[1]);
                    if (!DatabaseReportManager.getInstance().containsId(id)) {
                        if (DatabaseReportManager.getInstance().softSolve(id, (Player) sender)) {
                            sender.sendMessage(formater.formatMessage("admins.commands.check.solved"));
                            return true;
                        }
                        sender.sendMessage(formater.formatMessage("admins.commands.check.incorrect_id"));
                        return true;
                    }

                    if (DatabaseReportManager.getInstance().getReport(id).isSolved()) {
                        sender.sendMessage(formater.formatMessage("admins.commands.check.is_solved"));
                        return true;
                    }
                    DatabaseReportManager.getInstance().getReport(id).solveReport((Player) sender);
                    sender.sendMessage(formater.formatMessage("admins.commands.check.solved"));
                    return true;
                }
                if (sender.hasPermission(config.getPerms("clear.all")) && args[0].equals("clear_all")) {
                    if (!config.isDatabaseEnabled()) {
                        sender.sendMessage(formater.formatMessage("disabled_database"));
                        return true;
                    }
                    DatabaseReportManager.getInstance().clearReports(0);
                    sender.sendMessage(formater.formatMessage("admins.commands.clear"));
                    return true;
                }
                if (sender.hasPermission(config.getPerms("clear.solved")) && args[0].equals("clear_solved")) {
                    if (!config.isDatabaseEnabled()) {
                        sender.sendMessage(formater.formatMessage("disabled_database"));
                        return true;
                    }
                    DatabaseReportManager.getInstance().clearReports(2);
                    sender.sendMessage(formater.formatMessage("admins.commands.clear"));
                    return true;
                }
                if (sender.hasPermission(config.getPerms("reload")) && args[0].equals("reload")) {
                    if (ConfigData.getInstance().isBungeeEnabled()) BungeeLoader.getInstance().unregisterBungeeChannel();
                    new ConfigData();
                    new HelpOPTBAPI();
                    new PlayerData();
                    new FormatMessages();

                    if (ConfigData.getInstance().isDatabaseEnabled()) Database.load();
                    if (ConfigData.getInstance().isBungeeEnabled()) new BungeeLoader(true);

                    new APILoader();

                    sender.sendMessage(formater.formatMessage("admins.commands.reload"));
                    return true;
                }
                if (sender.hasPermission(config.getPerms("update")) && args[0].equals("update")) {
                    UpdateChecker.getInstance().sendInfo(sender);
                    UpdateChecker.getInstance().sendLatest(sender);
                    return true;
                }
                if (!sender.hasPermission(config.getPerms("report")) || !sender.hasPermission("helpoptb.report")) { //TODO Remove this old permission in next updates
                    sender.sendMessage(formater.formatMessage("no_permission"));
                } else {

                    PlayerData playerData = PlayerData.getInstance();
                    if (playerData.canSend((Player) sender)) {
                        playerData.setTimer((Player) sender);

                        StringBuilder message = new StringBuilder();
                        for (String word : args) {
                            message.append(word).append(" ");
                        }

                        Report report = new Report((Player) sender, message.toString());
                        report.sendReport(true, true);
                    } else {
                        sender.sendMessage(formater.formatMessage("players.cooldown"));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
