package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.speedcubing.clutch.Core.NormalClutch;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        NormalClutch.Join(e.getPlayer(), true);
    }
}
