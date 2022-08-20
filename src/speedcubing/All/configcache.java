package speedcubing.All;

import org.bukkit.command.*;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.server.libs.GlobalString;

import java.util.ArrayList;
import java.util.List;

public class configcache  implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : Member.getMember(commandSender).lang]);
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

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
