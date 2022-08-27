package speedcubing.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.Member;
import speedcubing.server.speedcubingServer;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Member member = Member.getMember(player);
        member.clear(player.getWorld(), member.PlayerCoord, true);
        Bukkit.getScheduler().cancelTask(member.CountDownTask);
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
                    , "id=" + member.user.id);
            Member.users.remove(member.user.id);
        }).start();
    }
}
