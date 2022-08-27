package speedcubing.Core;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;
import speedcubing.All.configcache;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBar;
import speedcubing.lib.bukkit.WorldUtils;

import java.util.HashSet;
import java.util.Set;

public class InclinedClutch {
    public static final int hitcount = 36;
    public static final InventoryHolder KB = () -> null;
    public static final Set<Integer> rooms = new HashSet<>();

    public static void Join(Player player) {
        int room = -1;
        for (int h = 1; h < Integer.MAX_VALUE; h++) {
            if (!rooms.contains(h)) {
                rooms.add(h);
                room = h;
                break;
            }
        }
        Member member = Member.getMember(player);
        Bukkit.getScheduler().cancelTask(member.CountDownTask);
        member.VeloSettings = false;
        member.Length = -1;
        member.clear(Bukkit.getWorld("world"), member.PlayerCoord, true);
        int coord = 5 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        player.teleport(new Location(Bukkit.getWorld("inclined"), coord + 0.5, 100, coord + 0.5, 45, 0));
        WorldUtils.cloneArea(1, 99, -1, -1, 98, 1, coord + 1, 99, coord - 1, player.getWorld());
        member.reset3(player, coord);
        member.CountDownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
            int hitindex = 0;
            int hitdelay = 0;

            @Override
            public void run() {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (!player.getItemInHand().getType().equals(Material.INK_SACK)) {
                        if (member.CurrentDelay == 0) {
                            if (hitdelay <= 0) {
                                if (hitindex == hitcount || member.KbCache2[hitindex] == 0) {
                                    member.reset();
                                    hitindex = 0;
                                } else {
                                    int sub = 2 * (member.KbCache2[hitindex] - 1);
                                    double velo = configcache.ReduceKB[sub + 10];
                                    player.setVelocity(new Vector(velo, configcache.ReduceKB[sub + 11] * (member.integerCache[3] / 1000D), -velo));
                                    player.damage(0);
                                    player.setLevel(hitindex + 1);
                                    SideBar.getSidebar(player).setLine(StringList.SideBarInclined[member.user.lang][9] + (hitindex + 1), 9);
                                    if (member.display) {
                                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                        connection.sendPacket(new PacketPlayOutTitle(0, 60, 5));
                                        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText("§e" + (hitindex + 1))));
                                    }
                                    hitindex += 1;
                                    hitdelay = member.integerCache[4];
                                    PlayerUtils.sendActionBar(player, "§aGo!");
                                }
                            }
                            hitdelay -= 1;
                        } else {
                            int currentdelay = member.CurrentDelay;
                            member.CurrentDelay = currentdelay - 1;
                            if (currentdelay % 2 == 0)
                                PlayerUtils.sendActionBar(player, "§e" + currentdelay / 20D);
                        }
                    } else {
                        int dd = member.CurrentDelay;
                        if (dd == 0) {
                            int b = member.Delay;
                            member.CurrentDelay = b;
                            dd = b;
                            hitdelay = 0;
                            hitindex = 0;
                        }
                        PlayerUtils.sendActionBar(player, "§e" + (dd % 2 != 0 && dd > 0 ? dd - 1 : dd) / 20D);
                    }
                }
            }
        }, 0, 1);
        String[] sidebar = StringList.SideBarInclined[member.user.lang];
        new SideBar(player, sidebar[0])
                .staticLine(sidebar[1], 9)
                .staticLine(sidebar[2], 8)
                .staticLine(sidebar[3], 7)
                .changeableLine(sidebar[4] + 0, 6)
                .changeableLine(sidebar[5] + 0, 5)
                .changeableLine(sidebar[6] + 0, 4)
                .staticLine(sidebar[7], 3)
                .staticLine(sidebar[8], 2)
                .changeableLine(sidebar[9] + 0, 1)
                .staticLine(sidebar[10], 0).create();
        member.Length2 = new int[]{coord, coord};
        Inventory inventory = player.getInventory();
        inventory.setItem(0, ItemStackList.Blocks);
        inventory.setItem(8, ItemStackList.itemstacks[member.user.lang]);
        inventory.setItem(7, ItemStackList.join[member.user.lang][0]);
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 5) - (Math.abs(z / 10) + z / 10) / (z / 5);
    }
}
