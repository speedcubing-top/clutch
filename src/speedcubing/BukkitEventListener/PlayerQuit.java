package speedcubing.BukkitEventListener;

import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.Core.Core;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.Main;

import java.util.UUID;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid)) {
            World world = player.getWorld();
            switch (world.getName()) {
                case "world": {
                    NormalClutch.Length.remove(uuid);
                    NormalClutch.used -= 1;
                    NormalClutch.rooms[NormalClutch.coordToRoom(Core.PlayerCoord.get(uuid))] = true;
                    for (BlockPosition b : Core.Blocks.get(uuid)) {
                        world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
                    }
                }
                break;
                case "inclined": {
                    InclinedClutch.Length.remove(uuid);
                    InclinedClutch.used -= 1;
                    InclinedClutch.rooms[InclinedClutch.coordToRoom(Core.PlayerCoord.get(uuid))] = true;
                    for (BlockPosition b : Core.Blocks.get(uuid)) {
                        world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
                    }
                    player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 1000, 0.5, 0, 0));
                }
                break;
            }
            Bukkit.getScheduler().cancelTask(Core.CountDownTask.get(uuid));
            Core.CountDownTask.remove(uuid);
            Main.VeloSettings.remove(uuid);
            Core.Delay.remove(uuid);
            Core.CurrentDelay.remove(uuid);
            Core.Blockplaced.remove(uuid);
            Core.Blocks.remove(uuid);
            Core.PlayerCoord.remove(uuid);
            new Thread(() -> {
                String str = "";
                for (Integer ints : NormalClutch.KbCache.get(uuid)) {
                    if (ints == 0)
                        break;
                    str += ints;
                }
                String str2 = "";
                for (Integer ints : InclinedClutch.KbCache.get(uuid)) {
                    if (ints == 0)
                        break;
                    str2 += ints;
                }
                Integer[] ints = Core.integerCache.get(uuid);
                speedcubingServer.connection.update("playersdata",
                        "clutch_blockskin=" + ints[0] +
                                ",clutch_normal_ver=" + ints[1] +
                                ",clutch_normal_hitdelay=" + ints[2] +
                                ",clutch_inclined_ver=" + ints[3] +
                                ",clutch_inclined_hitdelay=" + ints[4] +
                                ",clutch_normal_hits='" + str + "'" +
                                ",clutch_inclined_hits='" + str2 + "'"
                        , "uuid='" + uuid + "'");
                Core.integerCache.remove(uuid);
                InclinedClutch.KbCache.remove(uuid);
                NormalClutch.KbCache.remove(uuid);
            }).start();
        }
    }
}
