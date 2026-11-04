package top.speedcubing.clutch.bukkitlistener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
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
        member.cancelInput();
        member.clear(member.PlayerCoord, true);
        Bukkit.getScheduler().cancelTask(member.CountDownTask);
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
        String update = "clutch_normal_ver=" + ints.get(0) +
                ",clutch_normal_hitdelay=" + ints.get(1) +
                ",clutch_inclined_ver=" + ints.get(2) +
                ",clutch_inclined_hitdelay=" + ints.get(3) +
                ",clutch_normal_hits='" + str + "'" +
                ",clutch_inclined_hits='" + str2 + "'";
        // remove synchronously so a fast rejoin's new Member isn't deleted by a late async remove
        Member.users.remove(member.user.id);
        int id = member.user.id;
        CompletableFuture<Void> save = CompletableFuture.runAsync(() -> member.user.dbUpdate(update), speedcubingServer.scheduledPool);
        Member.pendingSaves.put(id, save);
        save.whenComplete((v, t) -> {
            Member.pendingSaves.remove(id, save);
            if (t != null)
                Bukkit.getLogger().log(Level.SEVERE, "clutch: failed to save settings of user " + id, t);
        });
    }
}
