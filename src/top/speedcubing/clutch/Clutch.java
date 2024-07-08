package top.speedcubing.clutch;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.clutch.BukkitEventListener.BlockBreak;
import top.speedcubing.clutch.BukkitEventListener.BlockPlace;
import top.speedcubing.clutch.BukkitEventListener.EntityDamage;
import top.speedcubing.clutch.BukkitEventListener.InventoryClick;
import top.speedcubing.clutch.BukkitEventListener.PlayerDropItem;
import top.speedcubing.clutch.BukkitEventListener.PlayerInteract;
import top.speedcubing.clutch.BukkitEventListener.PlayerJoin;
import top.speedcubing.clutch.BukkitEventListener.PlayerMove;
import top.speedcubing.clutch.BukkitEventListener.PlayerQuit;
import top.speedcubing.clutch.BukkitEventListener.PlayerSpawnLocation;
import top.speedcubing.clutch.Core.InclinedClutch;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.clutch.List.ItemStackList;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.server.speedcubingServer;
import top.speedcubing.server.utils.MapManager;

public class Clutch extends JavaPlugin {

    public static double[] kb;

    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "clutch");
        CubingEventManager.registerListeners(new ServerEvent());
        new ConfigReloadEvent().call();
        registerListeners();
        registerWorlds();

        new ItemStackList().Load();
    }

    private void registerListeners() {
        speedcubingServer.registerListeners(
                new PlayerJoin(),
                new PlayerQuit(),
                new BlockPlace(),
                new InventoryClick(),
                new PlayerMove(),
                new EntityDamage(),
                new BlockBreak(),
                new PlayerDropItem(),
                new PlayerInteract(),
                new PlayerSpawnLocation()
        );
    }

    private void registerWorlds() {
        try {
            MapManager.install("clutch_normal_gamz", NormalClutch.map, Difficulty.PEACEFUL, 6000, false);
            MapManager.install("clutch_inclined_cubing", InclinedClutch.map, Difficulty.PEACEFUL, 6000, false);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }
}
