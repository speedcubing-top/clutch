package speedcubing.Core;

import net.minecraft.server.v1_8_R3.BlockPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Core {
    public static final Map<UUID, Integer> Blockplaced = new HashMap<>();
    public static final Map<UUID, Set<BlockPosition>> Blocks = new HashMap<>();
    public static final Map<UUID, Integer> PlayerCoord = new HashMap<>();
    public static final Map<UUID, Integer> Delay = new HashMap<>();
    public static final Map<UUID, Integer> CurrentDelay = new HashMap<>();
    public static final Map<UUID, Integer> CountDownTask = new HashMap<>();
    public static final Map<UUID, Integer[]> integerCache = new HashMap<>();
}
