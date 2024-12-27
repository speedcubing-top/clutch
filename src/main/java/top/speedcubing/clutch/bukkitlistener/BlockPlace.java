package top.speedcubing.clutch.bukkitlistener;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import top.speedcubing.clutch.core.InclinedClutch;
import top.speedcubing.clutch.core.NormalClutch;
import top.speedcubing.clutch.list.ItemStackList;
import top.speedcubing.clutch.list.StringList;
import top.speedcubing.clutch.Member;
import top.speedcubing.lib.bukkit.SideBar;

public class BlockPlace implements Listener {
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            switch (player.getWorld().getName()) {
                case NormalClutch.map: {
                    Block block = e.getBlock();
                    int x = block.getX();
                    Member member = Member.getMember(player);
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
                            SideBar.getSidebar(player).setLineByScore(StringList.SideBarNormal[member.user.lang][4] + member.Length, 4);
                        }
                    }
                }
                break;
                case InclinedClutch.map: {
                    Block block = e.getBlock();
                    int z = block.getZ();
                    int x = block.getX();
                    Member member = Member.getMember(player);
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
                        boolean better = i[0] < x;
                        boolean better2 = i[1] > z;
                        if (better) {
                            i[0] = x;
                            sideBar.setLineByScore(sidebar[4] + realx, 6);
                        }
                        if (better2) {
                            i[1] = z;
                            sideBar.setLineByScore(sidebar[5] + realz, 5);
                        }
                        if (better || better2)
                            sideBar.setLineByScore(sidebar[6] + Math.round(Math.sqrt(realz * realz + realx * realx)), 4);
                    }
                }
                break;
            }
        }
    }
}
