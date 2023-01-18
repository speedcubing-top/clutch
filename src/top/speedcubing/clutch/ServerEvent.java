package top.speedcubing.clutch;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.speedcubing.lib.bukkit.event.PlayInEvent;
import top.speedcubing.lib.eventbus.LibEventHandler;
import top.speedcubing.lib.utils.Console;
import top.speedcubing.server.config;
import top.speedcubing.server.events.ConfigReloadEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.libs.GlobalString;
import top.speedcubing.server.speedcubingServer;

public class ServerEvent {
    @LibEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.packet instanceof PacketPlayInChat) {
            Player player = e.player;
            String message = ((PacketPlayInChat) e.packet).a();
            Member member = Member.getMember(player);
            if (member.VeloSettings) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        player.sendMessage(GlobalString.integerOutOfRange[member.user.lang].replace("%min%", "0.000").replace("%max%", "5.000"));
                    else {
                        member.integerCache[player.getWorld().getName().equals("world") ? 1 : 3] = x;
                        member.VeloSettings = false;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (message.charAt(0) != '/') {
                e.isCancelled = true;
                String[] formats = speedcubingServer.getFormat(member.user.rank);
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat;
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                speedcubingServer.globalChat(Bukkit.getOnlinePlayers(), player, out,out);
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
            }
        }
    }

    @LibEventHandler
    public void ToggleFlyEvent(ToggleFlyEvent e) {
        e.isCancelled = true;
        Member.getMember(e.player).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @LibEventHandler
    public void ConfigReloadEvent(ConfigReloadEvent e) {
        JsonObject object = config.config.getAsJsonObject("clutchkb");
        Clutch.kb = new double[]{
                object.get("low").getAsJsonArray().get(0).getAsDouble(),
                object.get("low").getAsJsonArray().get(1).getAsDouble(),
                object.get("mid").getAsJsonArray().get(0).getAsDouble(),
                object.get("mid").getAsJsonArray().get(1).getAsDouble(),
                object.get("diff").getAsJsonArray().get(0).getAsDouble(),
                object.get("diff").getAsJsonArray().get(1).getAsDouble(),
                object.get("high").getAsJsonArray().get(0).getAsDouble(),
                object.get("high").getAsJsonArray().get(1).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(0).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(1).getAsDouble(),
                object.get("low").getAsJsonArray().get(2).getAsDouble(),
                object.get("low").getAsJsonArray().get(3).getAsDouble(),
                object.get("mid").getAsJsonArray().get(2).getAsDouble(),
                object.get("mid").getAsJsonArray().get(3).getAsDouble(),
                object.get("diff").getAsJsonArray().get(2).getAsDouble(),
                object.get("diff").getAsJsonArray().get(3).getAsDouble(),
                object.get("high").getAsJsonArray().get(2).getAsDouble(),
                object.get("high").getAsJsonArray().get(3).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(2).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(3).getAsDouble()
        };
    }
}
