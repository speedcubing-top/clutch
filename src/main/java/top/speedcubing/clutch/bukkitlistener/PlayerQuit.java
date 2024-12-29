package top.speedcubing.clutch.bukkitlistener;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import top.speedcubing.clutch.Member;
import top.speedcubing.server.speedcubingServer;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Member member = Member.getMember(player);
        member.clear(player.getWorld(), member.PlayerCoord, true);
        Bukkit.getScheduler().cancelTask(member.CountDownTask);
        speedcubingServer.scheduledPool.execute(() -> {
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
            List<Integer> ints = member.integerCache;
            member.user.dbUpdate(
                    "clutch_blockskin=" + ints.get(0) +
                            ",clutch_normal_ver=" + ints.get(1) +
                            ",clutch_normal_hitdelay=" + ints.get(2) +
                            ",clutch_inclined_ver=" + ints.get(3) +
                            ",clutch_inclined_hitdelay=" + ints.get(4) +
                            ",clutch_normal_hits='" + str + "'" +
                            ",clutch_inclined_hits='" + str2 + "'"
            );
            Member.users.remove(member.user.id);
        });
    }
}
