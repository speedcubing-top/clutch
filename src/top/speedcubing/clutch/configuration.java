package top.speedcubing.clutch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import top.speedcubing.server.libs.GlobalString;

public class configuration implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : Member.getMember(commandSender).user.lang]);
        reload();
        return true;
    }

    public void reload() {
        Main.getPlugin(Main.class).reloadConfig();
        ReduceKB = new double[]{
                Main.getPlugin(Main.class).getConfig().getDouble("kb.low.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.low.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.mid.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.mid.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.difficult.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.difficult.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.high.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.high.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.extreme.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.extreme.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.low2.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.low2.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.mid2.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.mid2.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.difficult2.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.difficult2.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.high2.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.high2.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.extreme2.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.extreme2.ver")
        };
    }

    public static double[] ReduceKB;
}
