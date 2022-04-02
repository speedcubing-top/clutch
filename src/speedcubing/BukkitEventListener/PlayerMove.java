package speedcubing.BukkitEventListener;

import cubing.lib.bukkit.PlayerUtils;
import cubingserver.libs.User;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.Core.Core;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;

import java.util.UUID;

public class PlayerMove implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            World world = player.getWorld();
            switch (world.getName()) {
                case "world": {
                    UUID uuid = player.getUniqueId();
                    int coord = Core.PlayerCoord.get(uuid);
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getZ() - coord - 0.5) > 5.5) {
                        int lang = User.getLang(uuid);
                        Scoreboard scoreboard = player.getScoreboard();
                        String[] sidebar = StringList.SideBarNormal[lang];
                        scoreboard.getTeam(sidebar[4]).setSuffix("0");
                        scoreboard.getTeam(sidebar[1]).setSuffix("0");
                        NormalClutch.Length.put(uuid, 0);
                        Core.CurrentDelay.put(uuid, Core.Delay.get(uuid));
                        Core.clear(uuid,world);
                        player.setLevel(0);
                        player.closeInventory();
                        Core.Blockplaced.put(uuid, 0);
                        Core.Blocks.get(uuid).clear();
                        player.getInventory().setItem(0, ItemStackList.Blocks);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, 0.5, 100, coord + 0.5, 90, 0));
                    }
                }
                break;
                case "inclined":
                    UUID uuid = player.getUniqueId();
                    int coord = Core.PlayerCoord.get(uuid);
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getX() + to.getZ() - 2 * coord - 1) > 9.5) {
                        int lang = User.getLang(uuid);
                        Scoreboard scoreboard = player.getScoreboard();
                        String[] sidebar = StringList.SideBarInclined[lang];
                        scoreboard.getTeam(sidebar[7]).setSuffix("0");
                        scoreboard.getTeam(sidebar[6]).setSuffix("0");
                        scoreboard.getTeam(sidebar[5]).setSuffix("0");
                        scoreboard.getTeam(sidebar[1]).setSuffix("0");
                        InclinedClutch.Length.put(uuid, new Integer[]{coord, coord});
                        Core.CurrentDelay.put(uuid, Core.Delay.get(uuid));
                        Core.clear(uuid,world);
                        player.setLevel(0);
                        player.closeInventory();
                        Core.Blockplaced.put(uuid, 0);
                        Core.Blocks.get(uuid).clear();
                        player.getInventory().setItem(0, ItemStackList.Blocks);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, coord + 0.5, 100, coord + 0.5, 45, 0));
                    }
                    break;
            }
        }
    }
}
