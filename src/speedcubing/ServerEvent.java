package speedcubing;

import cubing.lib.bukkit.packetwrapper.OutPlayerListHeaderFooter;
import cubing.lib.utils.Console;
import cubing.lib.bukkit.Event.ServerEventHandler;
import cubing.lib.bukkit.Event.events.PlayInChatEvent;
import cubingserver.StringList.GlobalString;
import cubingserver.customEvents.UDPEvent;
import cubingserver.libs.User;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import speedcubing.Core.Core;

import java.util.UUID;

public class ServerEvent {
    @ServerEventHandler
    public void UDPEvent(UDPEvent e) {
        String[] rs = e.rs;
        if (rs[0].equals("o") && rs[1].equals("1")) {
            speedcubingServer.AllPlayers = Integer.parseInt(rs[2]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                int lang = User.getLang(p.getUniqueId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(OutPlayerListHeaderFooter.a(GlobalString.LobbyTabList[0][lang], GlobalString.LobbyTabList[1][lang].replace("%int%", rs[2])));
            }
        }
    }

    @ServerEventHandler
    public void ChatEvent(PlayInChatEvent e) {
        e.setCancelled(true);
        Player player = e.player;
        String message = e.packet.a();
        UUID uuid = player.getUniqueId();
        if (Main.VeloSettings.contains(uuid)) {
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage(GlobalString.integerOutOfRange[User.getLang(uuid)].replace("%min%", "0.000").replace("%max%", "5.000"));
                else {
                    Core.integerCache.get(uuid)[player.getWorld().getName().equals("world") ? 1 : 3] = x;
                    Main.VeloSettings.remove(uuid);
                }
            } catch (Exception exception) {
                player.sendMessage(GlobalString.invalidInteger[User.getLang(uuid)]);
            }
            return;
        }
        long time = System.currentTimeMillis();
        boolean spam = PlayerJoin.Canspam.contains(uuid);
        if (!spam && time - speedcubingServer.spam.get(uuid) < 500)
            player.sendMessage(GlobalString.spam[User.getLang(uuid)]);
        else if (message.charAt(0) != '/') {
            if (!spam && speedcubingServer.lastmsg.get(uuid).equals(message))
                player.sendMessage(GlobalString.samemsg[User.getLang(uuid)]);
            else {
                String[] formats = User.getFormat(User.getRank(uuid));
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                for (Player p : player.getWorld().getPlayers()) {
                    p.sendMessage(out[User.getLang(p.getUniqueId())]);
                }
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
                speedcubingServer.lastmsg.put(uuid, message);
            }
        } else e.setCancelled(false);
    }
}
