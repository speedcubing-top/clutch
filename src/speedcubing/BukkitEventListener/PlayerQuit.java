package speedcubing.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.server.listeners.PlayerJoin;
import speedcubing.server.speedcubingServer;

import java.util.UUID;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid)) {
            World world = player.getWorld();
            Member member = Member.getMember(uuid);
            switch (world.getName()) {
                case "world": {
                    NormalClutch.used -= 1;
                    NormalClutch.rooms[NormalClutch.coordToRoom(member.PlayerCoord)] = true;
                }
                break;
                case "inclined": {
                    InclinedClutch.used -= 1;
                    InclinedClutch.rooms[InclinedClutch.coordToRoom(member.PlayerCoord)] = true;
                }
                break;
            }
            member.clear(world);
            Bukkit.getScheduler().cancelTask(member.CountDownTask);
            Main.VeloSettings.remove(uuid);
            new Thread(() -> {
                StringBuilder str = new StringBuilder();
                for (int ints : member.KbCache) {
                    if (ints == 0)
                        break;
                    str.append(ints);
                }
                StringBuilder str2 = new StringBuilder();
                for (int ints : member.KbCache2) {
                    if (ints == 0)
                        break;
                    str2.append(ints);
                }
                int[] ints = member.integerCache;
                speedcubingServer.connection.update("playersdata",
                        "clutch_blockskin=" + ints[0] +
                                ",clutch_normal_ver=" + ints[1] +
                                ",clutch_normal_hitdelay=" + ints[2] +
                                ",clutch_inclined_ver=" + ints[3] +
                                ",clutch_inclined_hitdelay=" + ints[4] +
                                ",clutch_normal_hits='" + str + "'" +
                                ",clutch_inclined_hits='" + str2 + "'"
                        , "uuid='" + uuid + "'");
                Member.users.remove(uuid);
            }).start();
        }
    }
}
