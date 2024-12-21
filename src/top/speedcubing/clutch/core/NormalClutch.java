package top.speedcubing.clutch.core;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;
import top.speedcubing.clutch.Clutch;
import top.speedcubing.clutch.Member;
import top.speedcubing.clutch.list.ItemStackList;
import top.speedcubing.clutch.list.StringList;
import top.speedcubing.lib.bukkit.PlayerUtils;
import top.speedcubing.lib.bukkit.SideBar;
import top.speedcubing.lib.bukkit.WorldUtils;
import top.speedcubing.server.speedcubingServer;

public class NormalClutch {
    public static final int hitcount = 36;
    public static final int hitPage = 2;
    public static final int actualHitCount = hitcount * hitPage;
    public static final InventoryHolder KB = () -> null;
    public static final Set<Integer> rooms = new HashSet<>();

    public static final String map = "map_clutch_normal";

    public static void Join(Player player, boolean firstjoin) {
        int room = -1;
        for (int h = 1; h < Integer.MAX_VALUE; h++) {
            if (!rooms.contains(h)) {
                rooms.add(h);
                room = h;
                break;
            }
        }
        Member member;
        if (firstjoin) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setMaximumNoDamageTicks(1);
            member = new Member(player);
        } else {
            member = Member.getMember(player);
            Bukkit.getScheduler().cancelTask(member.CountDownTask);
            member.VeloSettings = false;
            member.Length2 = null;
            member.clear(Bukkit.getWorld(InclinedClutch.map), member.PlayerCoord, true);
        }
        int coord = 6 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        player.teleport(new Location(Bukkit.getWorld(map), 0.5, 100, coord + 0.5, 90, 0));
        WorldUtils.cloneArea(-2, 99, 1, 0, 98, -1, -2, 99, 1 + coord, player.getWorld());
        member.reset3(player, coord);
        member.CountDownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Clutch.getPlugin(Clutch.class), new Runnable() {
            int hitindex = 0;
            int hitdelay = 0;

            @Override
            public void run() {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (!player.getItemInHand().getType().equals(Material.INK_SACK)) { //Pause
                        if (member.CurrentDelay == 0) {
                            if (hitdelay <= 0) {
                                if (hitindex == hitcount || member.KbCache[hitindex] == 0) {
                                    member.reset();
                                    hitindex = 0;
                                } else {
                                    int sub = 2 * (member.KbCache[hitindex] - 1);
                                    player.setVelocity(new Vector(Clutch.kb[sub], Clutch.kb[sub + 1] * (member.integerCache[1] / 1000D), 0));
                                    player.damage(0);
                                    player.setLevel(hitindex + 1);
                                    SideBar.getSidebar(player).setLineByScore(StringList.SideBarNormal[member.user.lang][7] + (hitindex + 1), 1);
                                    if (member.display) {
                                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                        connection.sendPacket(new PacketPlayOutTitle(0, 60, 5));
                                        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText("§e" + (hitindex + 1))));
                                    }
                                    hitindex += 1;
                                    hitdelay = member.integerCache[2];
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
        String[] sidebar = StringList.SideBarNormal[member.user.lang];
        new SideBar(player, sidebar[0])
                .staticLine(sidebar[1], 7)
                .staticLine(sidebar[2], 6)
                .staticLine(sidebar[3], 5)
                .changeableLine(sidebar[4] + 0, 4)
                .staticLine(sidebar[5], 3)
                .staticLine(sidebar[6], 2)
                .changeableLine(sidebar[7] + 0, 1)
                .staticLine(sidebar[8], 0).create();
        member.Length = 0;
        speedcubingServer.scheduledPool.execute(() -> {
            if (firstjoin) {
                member.integerCache = ArrayUtils.toPrimitive(member.user.dbSelect("clutch_blockskin,clutch_normal_ver,clutch_normal_hitdelay,clutch_inclined_ver,clutch_inclined_hitdelay").getIntArray());
                String[] hits = member.user.dbSelect("clutch_normal_hits,clutch_inclined_hits").getStringArray();
                int size = hits[0].length();
                for (int i = 0; i < actualHitCount; i++) {
                    member.KbCache[i] = i < size ? hits[0].charAt(i) - 48 : 0;
                }
                int size2 = hits[1].length();
                for (int i = 0; i < actualHitCount; i++) {
                    member.KbCache2[i] = i < size2 ? hits[1].charAt(i) - 48 : 0;
                }
            }
            Inventory inventory = player.getInventory();
            inventory.setItem(0, ItemStackList.Blocks);
            inventory.setItem(8, ItemStackList.itemstacks[member.user.lang]);
            inventory.setItem(7, ItemStackList.join[member.user.lang][1]);
        });
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 6) - (Math.abs(z / 12) + z / 12) / (z / 6);
    }
}