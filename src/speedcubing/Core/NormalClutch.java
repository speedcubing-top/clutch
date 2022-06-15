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
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import speedcubing.All.configcache;
import speedcubing.BukkitEventListener.PlayerInteract;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBarAPI;
import speedcubing.lib.bukkit.WorldUtils;
import speedcubing.server.speedcubingServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NormalClutch {
    public static final int hitcount = 36;
    public static final InventoryHolder KB = () -> null;
    public static final Set<Integer> rooms = new HashSet<>();

    public static void Join(Member member, Player player, boolean firstjoin) {
        int room = -1;
        for (int h = 1; h < Integer.MAX_VALUE; h++) {
            if (!rooms.contains(h)) {
                rooms.add(h);
                room = h;
                break;
            }
        }
        UUID uuid = player.getUniqueId();
        if (firstjoin) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setMaximumNoDamageTicks(1);
        } else {
            Bukkit.getScheduler().cancelTask(member.CountDownTask);
            Main.VeloSettings.remove(uuid);
            member.Length2 = null;
            member.clear(Bukkit.getWorld("inclined"), member.PlayerCoord, true);
        }
        int coord = 6 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, coord + 0.5, 90, 0));
        WorldUtils.cloneArea(-2, 99, 1, 0, 98, -1, -2, 99, 1 + coord, player.getWorld());
        member.reset3(player, coord);
        member.CountDownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
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
                                    player.setVelocity(new Vector(configcache.ReduceKB[sub], configcache.ReduceKB[sub + 1] * (member.integerCache[1] / 1000D), 0));
                                    player.damage(0);
                                    player.setLevel(hitindex + 1);
                                    player.getScoreboard().getTeam(StringList.SideBarNormal[member.user.lang][1]).setSuffix(Integer.toString(hitindex + 1));
                                    if (PlayerInteract.display.contains(uuid)) {
                                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                        connection.sendPacket(new PacketPlayOutTitle(0, 60, 5));
                                        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText("§e" + (hitindex + 1))));
                                    }
                                    hitindex += 1;
                                    hitdelay = member.integerCache[2];
                                    PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§aGo!");
                                }
                            }
                            hitdelay -= 1;
                        } else {
                            int currentdelay = member.CurrentDelay;
                            member.CurrentDelay = currentdelay - 1;
                            if (currentdelay % 2 == 0)
                                PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§e" + currentdelay / 20D);
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
                        PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§e" + (dd % 2 != 0 && dd > 0 ? dd - 1 : dd) / 20D);
                    }
                }
            }
        }, 0, 1);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String[] sidebar = StringList.SideBarNormal[member.user.lang];
        objective.setDisplayName(sidebar[8]);
        SideBarAPI.sidebarTeams(scoreboard, new String[][]{
                {sidebar[7]},
                {sidebar[6]},
                {sidebar[5]},
                {"", sidebar[4], "0"},
                {sidebar[2]},
                {sidebar[3]},
                {sidebar[2]},
                {"", sidebar[1], "0"},
                {sidebar[0]},
        }, new int[]{7, 6, 5, 4, 3, 2, 1, 0
        });
        player.setScoreboard(scoreboard);
        member.Length = 0;
        new Thread(() -> {
            if (firstjoin) {
                member.integerCache = speedcubingServer.connection.selectInts("playersdata", "clutch_blockskin,clutch_normal_ver,clutch_normal_hitdelay,clutch_inclined_ver,clutch_inclined_hitdelay", "uuid='" + uuid + "'");
                String[] hits = speedcubingServer.connection.selectStrings("playersdata", "clutch_normal_hits,clutch_inclined_hits", "uuid='" + uuid + "'");
                int size = hits[0].length();
                for (int i = 0; i < hitcount; i++) {
                    member.KbCache[i] = i < size ? hits[0].charAt(i) - 48 : 0;
                }
                int size2 = hits[1].length();
                for (int i = 0; i < hitcount; i++) {
                    member.KbCache2[i] = i < size2 ? hits[1].charAt(i) - 48 : 0;
                }
            }
            Inventory inventory = player.getInventory();
            inventory.setItem(0, ItemStackList.Blocks);
            inventory.setItem(8, ItemStackList.itemstacks[member.user.lang]);
            inventory.setItem(7, ItemStackList.join[member.user.lang][1]);
        }).start();
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 6) - (Math.abs(z / 12) + z / 12) / (z / 6);
    }
}