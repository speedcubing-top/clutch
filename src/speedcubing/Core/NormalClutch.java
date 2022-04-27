package speedcubing.Core;

import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBarAPI;
import speedcubing.server.PluginMessage;
import speedcubing.server.StringList.GlobalString;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.*;
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

import java.util.*;

public class NormalClutch {

    public static final int size = 11;
    public static int used = 0;
    public static final int hitcount = 36;
    public static final InventoryHolder KB = () -> null;
    public static final boolean[] rooms = new boolean[size];

    public static final Map<UUID, Integer> Length = new HashMap<>();
    public static final Map<UUID, Integer[]> KbCache = new HashMap<>();

    public void Load() {
        for (int i = 0; i < size; i++) {
            rooms[i] = true;
        }
    }

    public static void Join(Player player, boolean firstjoin) {
        if (used == size) {
            player.sendMessage(GlobalString.NoEnoughServers[User.getLang(player.getUniqueId())]);
            if (firstjoin)
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PluginMessage.switchServer(player, "lobby");
                    }
                }, 500);
            return;
        }
        int room = -1;
        for (int h = 0; h < size; h++) {
            if (rooms[h]) {
                room = h;
                break;
            }
        }
        rooms[room] = false;
        used += 1;
        UUID uuid = player.getUniqueId();
        int lang = User.getLang(uuid);

        if (firstjoin) {
            Core.Blocks.put(uuid, new HashSet<>());
            player.setGameMode(GameMode.SURVIVAL);
            player.setMaximumNoDamageTicks(1);
        } else {
            Bukkit.getScheduler().cancelTask(Core.CountDownTask.get(uuid));
            Main.VeloSettings.remove(uuid);
            InclinedClutch.Length.remove(uuid);
            InclinedClutch.used -= 1;
            InclinedClutch.rooms[InclinedClutch.coordToRoom(Core.PlayerCoord.get(uuid))] = true;
            Core.clear(uuid,Bukkit.getWorld("inclined"));
        }
        int coord = 6 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        Core.Blockplaced.put(uuid, 0);
        Core.PlayerCoord.put(uuid, coord);
        player.setLevel(0);
        player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100, coord + 0.5, 90, 0));
        player.getInventory().setHeldItemSlot(0);
        Core.CurrentDelay.put(uuid, 100);
        Core.Delay.put(uuid, 100);
        Core.CountDownTask.put(uuid, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
            int hitindex = 0;
            int hitdelay = 0;

            @Override
            public void run() {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (!player.getItemInHand().getType().equals(Material.INK_SACK)) { //Pause
                        if (Core.CurrentDelay.get(uuid) == 0) {
                            if (hitdelay <= 0) {
                                if (hitindex == hitcount || KbCache.get(uuid)[hitindex] == 0) {
                                    Core.CurrentDelay.put(uuid, Core.Delay.get(uuid));
                                    hitindex = 0;
                                } else {
                                    int sub = 2 * (KbCache.get(uuid)[hitindex] - 1);
                                    player.setVelocity(new Vector(configcache.ReduceKB[sub], configcache.ReduceKB[sub + 1] * (Core.integerCache.get(uuid)[1] / 1000D), 0));
                                    player.damage(0);
                                    player.setLevel(hitindex + 1);
                                    player.getScoreboard().getTeam(StringList.SideBarNormal[lang][1]).setSuffix(Integer.toString(hitindex + 1));
                                    if (PlayerInteract.display.contains(uuid)) {
                                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                        connection.sendPacket(new PacketPlayOutTitle(0, 60, 5));
                                        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText("§e" + (hitindex + 1))));
                                    }
                                    hitindex += 1;
                                    hitdelay = Core.integerCache.get(uuid)[2];
                                    PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§aGo!");
                                }
                            }
                            hitdelay -= 1;
                        } else {
                            int currentdelay = Core.CurrentDelay.get(uuid);
                            Core.CurrentDelay.put(uuid, currentdelay - 1);
                            if (currentdelay % 2 == 0) {
                                PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§e" + currentdelay / 20D);
                                if (currentdelay == 0)
                                    hitdelay = 0;
                            }
                        }
                    } else {
                        int dd = Core.CurrentDelay.get(uuid);
                        if (dd == 0) {
                            int b = Core.Delay.get(uuid);
                            Core.CurrentDelay.put(uuid, b);
                            dd = b;
                            hitdelay = 0;
                            hitindex = 0;
                        }
                        PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§e" + (dd % 2 != 0 && dd > 0 ? dd - 1 : dd) / 20D);
                    }
                }
            }
        }, 0, 1));
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String[] sidebar = StringList.SideBarNormal[User.getLang(uuid)];
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
        Length.put(uuid, 0);
        new Thread(() -> {
            if (firstjoin) {
                Core.integerCache.put(uuid, ArrayUtils.toObject(speedcubingServer.connection.selectInts("playersdata", "clutch_blockskin,clutch_normal_ver,clutch_normal_hitdelay,clutch_inclined_ver,clutch_inclined_hitdelay", "uuid='" + uuid + "'")));
                String[] hits = speedcubingServer.connection.selectStrings("playersdata", "clutch_normal_hits,clutch_inclined_hits", "uuid='" + uuid + "'");
                Integer[] Hits = new Integer[hitcount];
                int size = hits[0].length();
                for (int i = 0; i < hitcount; i++) {
                    Hits[i] = i < size ? hits[0].charAt(i) - 48 : 0;
                }
                Integer[] Hits2 = new Integer[hitcount];
                int size2 = hits[1].length();
                for (int i = 0; i < hitcount; i++) {
                    Hits2[i] = i < size2 ? hits[1].charAt(i) - 48 : 0;
                }
                KbCache.put(uuid, Hits);
                InclinedClutch.KbCache.put(uuid, Hits2);
            }
            Inventory inventory = player.getInventory();
            inventory.setItem(0, ItemStackList.Blocks);
            inventory.setItem(8, ItemStackList.itemstacks[lang]);
            inventory.setItem(7, ItemStackList.join[lang][1]);
        }).start();
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 6) - (Math.abs(z / 12) + z / 12) / (z / 6);
    }
}