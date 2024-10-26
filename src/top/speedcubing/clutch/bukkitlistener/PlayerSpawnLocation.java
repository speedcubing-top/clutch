package top.speedcubing.clutch.bukkitlistener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import top.speedcubing.clutch.core.NormalClutch;

public class PlayerSpawnLocation implements Listener {
    @EventHandler
    public void PlayerSpawnLocationEvent(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(new Location(Bukkit.getWorld(NormalClutch.map), 0.5, 1000, 0.5, 0, 0));
    }
}
