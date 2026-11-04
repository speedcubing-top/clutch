package top.speedcubing.clutch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.speedcubing.clutch.core.InclinedClutch;
import top.speedcubing.clutch.core.NormalClutch;
import top.speedcubing.clutch.list.ItemStackList;
import top.speedcubing.lib.bukkit.WorldUtils;
import top.speedcubing.server.player.User;

public class Member {
    public static Map<Integer, Member> users = new HashMap<>();
    // async quit saves still in flight, keyed by user id; a rejoin waits on it before reading the DB
    public static final Map<Integer, CompletableFuture<Void>> pendingSaves = new ConcurrentHashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember(User.getUser(sender).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    public final User user;

    public int Blockplaced;
    public Set<BlockPosition> Blocks = new HashSet<>();
    public int PlayerCoord;
    public int Delay;
    public int CurrentDelay;
    public int CountDownTask;
    // clutch_normal_ver, clutch_normal_hitdelay, clutch_inclined_ver, clutch_inclined_hitdelay
    public final List<Integer> integerCache  = new ArrayList<>();
    public int Length;
    public int[] KbCache = new int[NormalClutch.actualHitCount];
    public int[] Length2 = new int[2];
    public int[] KbCache2 = new int[NormalClutch.actualHitCount];

    public boolean VeloSettings;
    public boolean inclined;
    public boolean display;

    public Member(Player player) {
        this.user = User.getUser(player);
        users.put(user.id, this);
    }

    public World modeWorld() {
        return Bukkit.getWorld(inclined ? InclinedClutch.map : NormalClutch.map);
    }

    // operates on the world of the mode the member is in, not the player's current world
    public void clear(int coord, boolean quit) {
        World world = modeWorld();
        if (quit) {
            if (inclined) {
                InclinedClutch.rooms.remove(InclinedClutch.coordToRoom(coord));
                WorldUtils.fillAir(coord - 1, 99, coord + 1, coord + 1, 98, coord - 1, world);
            } else {
                NormalClutch.rooms.remove(NormalClutch.coordToRoom(coord));
                WorldUtils.fillAir(0, 98, -1 + coord, -2, 99, 1 + coord, world);
            }
        }
        Blocks.forEach(a -> world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        Blocks.clear();
    }

    public void cancelInput() {
        if (VeloSettings) {
            VeloSettings = false;
            user.setInput(false);
        }
    }

    public void reset() {
        CurrentDelay = Delay;
    }

    public void reset2(Player player) {
        reset();
        clear(PlayerCoord, false);
        player.setLevel(0);
        player.closeInventory();
        Blockplaced = 0;
        player.getInventory().setItem(0, ItemStackList.Blocks);
    }

    public void reset3(Player player, int coord) {
        Blockplaced = 0;
        PlayerCoord = coord;
        CurrentDelay = 100;
        Delay = 100;
        player.setLevel(0);
        player.getInventory().setHeldItemSlot(0);
    }
}
