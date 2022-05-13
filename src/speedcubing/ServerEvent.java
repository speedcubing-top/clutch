package speedcubing;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import speedcubing.lib.bukkit.packetwrapper.OutPlayerListHeaderFooter;
import speedcubing.lib.utils.Console;
import speedcubing.server.events.UDPEvent;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;
import speedcubing.spigot.Event.ServerEventHandler;
import speedcubing.spigot.Event.events.PlayInChatEvent;

import java.util.UUID;

public class ServerEvent {
    @ServerEventHandler
    public void UDPEvent(UDPEvent e) {
        String[] rs = e.rs;
        if (rs[0].equals("o") && rs[1].equals("1")) {
            speedcubingServer.AllPlayers = Integer.parseInt(rs[2]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                Member m = Member.getMember(p.getUniqueId());
                if (m != null)
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(OutPlayerListHeaderFooter.a(GlobalString.LobbyTabList[0][m.lang], GlobalString.LobbyTabList[1][m.lang].replace("%int%", rs[2])));
            }
        }
    }

    @ServerEventHandler
    public void ChatEvent(PlayInChatEvent e) {
        Player player = e.player;
        String message = e.packet.a();
        UUID uuid = player.getUniqueId();
        if (Main.VeloSettings.contains(uuid)) {
            e.setCancelled(true);
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage(GlobalString.integerOutOfRange[User.getUser(uuid).lang].replace("%min%", "0.000").replace("%max%", "5.000"));
                else {
                    Member.getMember(uuid).integerCache[player.getWorld().getName().equals("world") ? 1 : 3] = x;
                    Main.VeloSettings.remove(uuid);
                    speedcubingServer.node(false, uuid);
                }
            } catch (Exception exception) {
                player.sendMessage(GlobalString.invalidInteger[User.getUser(uuid).lang]);
            }
        } else if (message.charAt(0) != '/') {
            e.setCancelled(true);
            String[] formats = speedcubingServer.getFormat(User.getUser(uuid).rank);
            String text = formats[0] + player.getName();
            String[] str = GlobalString.MessageFormat[0];
            String[] out = new String[]{
                    str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
            };
            Bukkit.getOnlinePlayers().forEach(a -> a.sendMessage(out[User.getUser(a.getUniqueId()).lang]));
            Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
        }
    }
}
