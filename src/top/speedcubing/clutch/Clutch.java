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
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.server.utils.MapManager;
import top.speedcubing.server.utils.config;

public class Clutch extends JavaPlugin {

    public static double[] kb;

    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "clutch");
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSpawnLocation(), this);

        new ItemStackList().Load();
        CubingEventManager.registerListeners(new ServerEvent());
        config.event.call();

        try {
            MapManager.install("clutch_normal_gamz", NormalClutch.map, Difficulty.PEACEFUL, 6000, false);
            MapManager.install("clutch_inclined_cubing", InclinedClutch.map, Difficulty.PEACEFUL, 6000, false);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }
}
