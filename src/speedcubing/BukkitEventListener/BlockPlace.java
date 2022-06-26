package speedcubing.BukkitEventListener;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Member;
import speedcubing.lib.bukkit.SideBar;

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
                    Member member = Member.getMember(uuid);
                    if (x < -2 || Math.abs(block.getZ() - member.PlayerCoord) > 6)
                        e.setCancelled(true);
                    else {
                        member.Blocks.add(new BlockPosition(x, block.getY(), block.getZ()));
                        member.Blockplaced += 1;
                        if (member.Blockplaced > 32) {
                            member.Blockplaced = 0;
                            player.getInventory().setItem(0, ItemStackList.Blocks);
                        }
                        if (member.Length < x) {
                            member.Length = x;
                            SideBar.getSidebar(player).setLine(StringList.SideBarNormal[member.user.lang][4] + member.Length, 4);
                        }
                    }
                }
                break;
                case "inclined": {
                    Block block = e.getBlock();
                    int z = block.getZ();
                    int x = block.getX();
                    UUID uuid = player.getUniqueId();
                    Member member = Member.getMember(uuid);
                    if (x - z < 0 || Math.abs(x + z - 2 * member.PlayerCoord) > 10)
                        e.setCancelled(true);
                    else {
                        member.Blocks.add(new BlockPosition(x, block.getY(), block.getZ()));
                        member.Blockplaced += 1;
                        if (member.Blockplaced > 32) {
                            member.Blockplaced = 0;
                            player.getInventory().setItem(0, ItemStackList.Blocks);
                        }
                        int realx = x - member.PlayerCoord;
                        int realz = member.PlayerCoord - z;
                        int[] i = member.Length2;
                        String[] sidebar = StringList.SideBarInclined[member.user.lang];
                        SideBar sideBar = SideBar.getSidebar(player);
                        if (i[0] < x) {
                            i[0] = x;
                            sideBar.setLine(sidebar[6] + realx, 4);
                        }
                        if (i[1] > z) {
                            i[1] = z;
                            sideBar.setLine(sidebar[5] + realz, 5);
                        }
                        sideBar.setLine(sidebar[6] + Math.round(Math.sqrt(realz * realz + realx * realx)), 6);
                    }
                }
                break;
            }
        }
    }
}
