package top.speedcubing.clutch.bukkitlistener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.speedcubing.clutch.core.NormalClutch;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        NormalClutch.Join(e.getPlayer(), true);
    }
}
