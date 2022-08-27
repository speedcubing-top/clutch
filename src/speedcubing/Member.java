package speedcubing;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.lib.bukkit.WorldUtils;
import speedcubing.server.libs.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Member {
    public static Map<Integer, Member> users = new HashMap<>();

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
    public int[] integerCache;
    public int Length;
    public int[] KbCache = new int[NormalClutch.hitcount];
    public int[] Length2 = new int[2];
    public int[] KbCache2 = new int[InclinedClutch.hitcount];

    public boolean VeloSettings;
    public boolean display;

    public Member(Player player) {
        this.user = User.getUser(player);
        users.put(user.id, this);
    }

    public void clear(World world, int coord, boolean quit) {
        if (quit)
            switch (world.getName()) {
                case "world":
                    NormalClutch.rooms.remove(NormalClutch.coordToRoom(coord));
                    WorldUtils.fillAir(0, 98, -1 + coord, -2, 99, 1 + coord, world);
                    break;
                case "inclined":
                    InclinedClutch.rooms.remove(InclinedClutch.coordToRoom(coord));
                    WorldUtils.fillAir(coord - 1, 99, coord + 1, coord + 1, 98, coord - 1, world);
                    break;
            }
        Blocks.forEach(a -> world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        Blocks.clear();
    }

    public void reset() {
        CurrentDelay = Delay;
    }

    public void reset2(Player player, World world) {
        reset();
        clear(world, PlayerCoord, false);
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
