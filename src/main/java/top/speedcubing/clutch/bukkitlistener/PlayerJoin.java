package top.speedcubing.clutch.bukkitlistener;

import java.util.concurrent.CompletableFuture;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.speedcubing.clutch.Member;
import top.speedcubing.clutch.core.NormalClutch;
import top.speedcubing.server.player.User;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        // a fast rejoin must not read the row before the previous session's quit save has landed
        CompletableFuture<Void> pendingSave = Member.pendingSaves.get(User.getUser(e.getPlayer()).id);
        if (pendingSave != null) {
            try {
                pendingSave.join();
            } catch (Exception ignored) {
                // failure is logged by PlayerQuit; load whatever the DB has
            }
        }
        NormalClutch.Join(e.getPlayer(), true);
    }
}
