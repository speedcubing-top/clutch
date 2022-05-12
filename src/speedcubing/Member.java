package speedcubing;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Material;
import org.bukkit.World;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.server.libs.User;

import java.util.*;

public class Member extends User {
    public static Map<UUID, Member> users = new HashMap<>();

    public static Member getMember(UUID uuid) {
        return users.get(uuid);
    }

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

    public Member(UUID uuid) {
        super.init(this, uuid);
        users.put(uuid, this);
    }

    public void clear(World world) {
        Blocks.forEach(a -> world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        Blocks.clear();
    }

    public void reset() {
        CurrentDelay = Delay;
    }
}
