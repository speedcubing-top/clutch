package top.speedcubing.clutch;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.clutch.BukkitEventListener.*;
import top.speedcubing.clutch.List.ItemStackList;
import top.speedcubing.lib.eventbus.CubingEventManager;
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

        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getWorld("world").setTime(6000);
        Bukkit.getWorld("world").setAutoSave(false);

        getServer().createWorld(new WorldCreator("inclined"));
        Bukkit.getWorld("inclined").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getWorld("inclined").setTime(6000);
        Bukkit.getWorld("inclined").setAutoSave(false);

        new ItemStackList().Load();
        CubingEventManager.registerListeners(new ServerEvent());
        config.event.call();
    }
}
