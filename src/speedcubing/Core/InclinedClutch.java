package speedcubing.Core;

import speedcubing.server.libs.GlobalString;
import speedcubing.server.libs.User;
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
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBarAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InclinedClutch {

    public static final int size = 11;
    public static int used = 0;
    public static final int hitcount = 36;
    public static final InventoryHolder KB = () -> null;
    public static final boolean[] rooms = new boolean[size];

    public static final Map<UUID, Integer[]> Length = new HashMap<>();
    public static final Map<UUID, Integer[]> KbCache = new HashMap<>();

    public void Load() {
        for (int i = 0; i < size; i++) {
            rooms[i] = true;
        }
    }

    public static void Join(Player player) {
        if (used == size) {
            player.sendMessage(GlobalString.NoEnoughServers[User.getUser(player.getUniqueId()).lang]);
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
        int lang = User.getUser(uuid).lang;
        Bukkit.getScheduler().cancelTask(Core.CountDownTask.get(uuid));
        Main.VeloSettings.remove(uuid);
        NormalClutch.Length.remove(uuid);
        NormalClutch.used -= 1;
        NormalClutch.rooms[NormalClutch.coordToRoom(Core.PlayerCoord.get(uuid))] = true;
        Core.clear(uuid, Bukkit.getWorld("world"));
        Core.Blockplaced.put(uuid, 0);

        int coord = 5 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        Core.PlayerCoord.put(uuid, coord);
        player.setLevel(0);
        player.teleport(new Location(Bukkit.getWorld("inclined"), coord + 0.5, 100, coord + 0.5, 45, 0));
        player.getInventory().setHeldItemSlot(0);
        Core.CurrentDelay.put(uuid, 100);
        Core.Delay.put(uuid, 100);
        Core.CountDownTask.put(uuid, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
            int hitindex = 0;
            int hitdelay = 0;

            @Override
            public void run() {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (!player.getItemInHand().getType().equals(Material.INK_SACK)) {
                        if (Core.CurrentDelay.get(uuid) == 0) {
                            if (hitdelay <= 0) {
                                if (hitindex == hitcount || KbCache.get(uuid)[hitindex] == 0) {
                                    Core.CurrentDelay.put(uuid, Core.Delay.get(uuid));
                                    hitindex = 0;
                                } else {
                                    int sub = 2 * (KbCache.get(uuid)[hitindex] - 1);
                                    double velo = configcache.ReduceKB[sub + 10];
                                    player.setVelocity(new Vector(velo, configcache.ReduceKB[sub + 11] * (Core.integerCache.get(uuid)[3] / 1000D), -velo));
                                    player.damage(0);
                                    player.setLevel(hitindex + 1);
                                    player.getScoreboard().getTeam(StringList.SideBarInclined[lang][1]).setSuffix(Integer.toString(hitindex + 1));
                                    if (PlayerInteract.display.contains(uuid)) {
                                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                        connection.sendPacket(new PacketPlayOutTitle(0, 60, 5));
                                        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText("§e" + (hitindex + 1))));
                                    }
                                    hitindex += 1;
                                    hitdelay = Core.integerCache.get(uuid)[4];
                                    PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§aGo!");
                                }
                            }
                            hitdelay -= 1;
                        } else {
                            int currentdelay = Core.CurrentDelay.get(uuid);
                            Core.CurrentDelay.put(uuid, currentdelay - 1);
                            if (currentdelay % 2 == 0)
                                PlayerUtils.sendActionBar(((CraftPlayer) player).getHandle().playerConnection, "§e" + currentdelay / 20D);
                            if (currentdelay == 0)
                                hitdelay = 0;
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
        String[] sidebar = StringList.SideBarInclined[lang];
        objective.setDisplayName(sidebar[11]);
        SideBarAPI.sidebarTeams(scoreboard, new String[][]{
                {sidebar[10]},
                {sidebar[9]},
                {sidebar[8]},
                {"", sidebar[7], "0"},
                {"", sidebar[6], "0"},
                {sidebar[4], sidebar[5], "0"},
                {sidebar[3]},
                {sidebar[2]},
                {"", sidebar[1], "0"},
                {sidebar[0]},
        }, new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0
        });
        player.setScoreboard(scoreboard);
        Length.put(uuid, new Integer[]{coord, coord});
        Inventory inventory = player.getInventory();
        inventory.setItem(0, ItemStackList.Blocks);
        inventory.setItem(8, ItemStackList.itemstacks[lang]);
        inventory.setItem(7, ItemStackList.join[lang][0]);
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 5) - (Math.abs(z / 10) + z / 10) / (z / 5);
    }
}
