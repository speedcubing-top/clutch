package speedcubing;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import speedcubing.lib.bukkit.event.PlayInEvent;
import speedcubing.lib.eventbus.LibEventHandler;
import speedcubing.lib.utils.Console;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;

import java.util.UUID;

public class ServerEvent {
    @LibEventHandler
    public void PlayInEvent(PlayInEvent e) {
        Player player = e.player;
        if (e.packet instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.packet).a();
            UUID uuid = player.getUniqueId();
            if (Main.VeloSettings.contains(uuid)) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        player.sendMessage(GlobalString.integerOutOfRange[Member.getMember(uuid).user.lang].replace("%min%", "0.000").replace("%max%", "5.000"));
                    else {
                        Member member = Member.getMember(uuid);
                        member.integerCache[player.getWorld().getName().equals("world") ? 1 : 3] = x;
                        Main.VeloSettings.remove(uuid);
                        speedcubingServer.node(false, uuid, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    player.sendMessage(GlobalString.invalidInteger[Member.getMember(uuid).user.lang]);
                }
            } else if (message.charAt(0) != '/') {
                e.isCancelled = true;
                String[] formats = speedcubingServer.getFormat(Member.getMember(uuid).user.rank);
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                Bukkit.getOnlinePlayers().forEach(a -> a.sendMessage(out[Member.getMember(a.getUniqueId()).user.lang]));
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
            }
        }
    }
}
