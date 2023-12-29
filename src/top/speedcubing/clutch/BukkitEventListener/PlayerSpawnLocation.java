package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    @EventHandler
    public void PlayerSpawnLocationEvent(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(new Location(Bukkit.getWorld("normal"), 0.5, 1000, 0.5, 0, 0));
    }
}
