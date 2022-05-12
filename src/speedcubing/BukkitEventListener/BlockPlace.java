package speedcubing.BukkitEventListener;

import speedcubing.server.libs.User;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.Core.Core;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;

import java.util.UUID;

public class BlockPlace implements Listener {
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            switch (player.getWorld().getName()) {
                case "world": {
                    Block block = e.getBlock();
                    int x = block.getX();
                    UUID uuid = player.getUniqueId();
                    if (x < -2 || Math.abs(block.getZ() - Core.PlayerCoord.get(uuid)) > 6)
                        e.setCancelled(true);
                    else {
                        Core.Blocks.get(uuid).add(new BlockPosition(x, block.getY(), block.getZ()));
                        Core.Blockplaced.put(uuid, Core.Blockplaced.get(uuid) + 1);
                        int lang = User.getUser(uuid).lang;
                        if (Core.Blockplaced.get(uuid) > 32) {
                            Core.Blockplaced.put(uuid, 0);
                            player.getInventory().setItem(0, ItemStackList.Blocks);
                        }
                        if (NormalClutch.Length.get(uuid) < x) {
                            NormalClutch.Length.put(uuid, x);
                            player.getScoreboard().getTeam(StringList.SideBarNormal[lang][4]).setSuffix(Integer.toString(NormalClutch.Length.get(uuid)));
                        }
                    }
                }
                break;
                case "inclined": {
                    Block block = e.getBlock();
                    int z = block.getZ();
                    int x = block.getX();
                    UUID uuid = player.getUniqueId();
                    int coord = Core.PlayerCoord.get(uuid);
                    if (x - z < 0 || Math.abs(x + z - 2 * coord) > 10)
                        e.setCancelled(true);
                    else {
                        Core.Blocks.get(uuid).add(new BlockPosition(x, block.getY(), z));
                        Core.Blockplaced.put(uuid, Core.Blockplaced.get(uuid) + 1);
                        int lang = User.getUser(uuid).lang;
                        if (Core.Blockplaced.get(uuid) > 32) {
                            Core.Blockplaced.put(uuid, 0);
                            player.getInventory().setItem(0, ItemStackList.Blocks);
                        }
                        Scoreboard scoreboard = player.getScoreboard();
                        int realx = x - coord;
                        int realz = coord - z;
                        Integer[] i = InclinedClutch.Length.get(uuid);
                        String[] sidebar = StringList.SideBarInclined[lang];
                        if (i[0] < x) {
                            i[0] = x;
                            scoreboard.getTeam(sidebar[7]).setSuffix(Integer.toString(realx));
                        }
                        if (i[1] > z) {
                            i[1] = z;
                            scoreboard.getTeam(sidebar[6]).setSuffix(Integer.toString(realz));
                        }
                        scoreboard.getTeam(sidebar[5]).setSuffix(String.valueOf(Math.round(Math.sqrt(realz * realz + realx * realx))));
                    }
                }
                break;
            }
        }
    }
}
