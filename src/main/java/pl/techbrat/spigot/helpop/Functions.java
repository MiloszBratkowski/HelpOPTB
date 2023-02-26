package pl.techbrat.spigot.helpop;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;

public class Functions {
    private static Functions instance;
    public static Functions getInstance() {
        return instance;
    }

    private static final int LIMIT = 10; //Number of reports on the one page

    protected Functions() {
        instance = this;
    }

    public void displayHistory(CommandSender sender, int type, Integer page) throws DatabaseDisabledException {
        ConfigData configData = ConfigData.getInstance();
        String title = configData.getMsg("admins.commands.history.title").replace("<page>", Integer.toString(page)).replace("<all_pages>", Integer.toString(getNumbersOfPages(type))).replace("<amount>", Integer.toString(getNumberOfReports(type)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
        for (RawReport report : DatabaseReportManager.getInstance().reportsFromDatabase(type, (page-1)*LIMIT, LIMIT)) {
            String messageOld = ChatColor.translateAlternateColorCodes('&', ConfigData.getInstance().getMsg("admins.commands.history.element").
                    replace("<id>", Integer.toString(report.getId())).
                    replace("<solved>", report.isSolved()?"&a✔":"&c✘").
                    replace("<solve_admin>", report.isSolved()?"&a"+report.getSolved():"&c✘").
                    replace("<date>", report.getDate()).
                    replace("<player>", report.getPlayerName()).
                    replace("<message>", report.getMessage()));
            if (HelpOPTB.getInstance().getVersionSymbol() >= 12) {
                TextComponent message = new TextComponent(messageOld);
                if (report.isSolved()) {
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configData.getMsg("admins.commands.history.hover_solve").replace("<player>", report.getSolved()))).create()));
                } else {
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', configData.getMsg("admins.commands.history.click_solve"))).create()));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helpop check " + report.getId()));
                }
                sender.spigot().sendMessage(message);
            } else {
                sender.sendMessage(messageOld);
            }
        }
    }

    public void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigData.getInstance().getMsg("admins.commands.help")));
    }

    //types: 0 - all, 1 - unsolved, 2 - solved
    protected int getNumberOfReports(int type) {
        String query = "SELECT COUNT(id) FROM "+Database.getInstance().getTable()+(type==1?" WHERE solved = -1":((type==2)?" WHERE solved NOT LIKE -1":""))+";";
        try {
            ResultSet result = Database.getInstance().execute(query);
            result.next();
            return result.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumbersOfPages(int type) {
        return (int)Math.ceil(getNumberOfReports(type)*1.0/LIMIT);
    }



    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
