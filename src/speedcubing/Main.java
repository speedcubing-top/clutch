package speedcubing;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import speedcubing.All.configcache;
import speedcubing.BukkitEventListener.*;
import speedcubing.List.ItemStackList;
import speedcubing.lib.eventbus.LibEventManager;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Main extends JavaPlugin {

    public void onEnable() {
        new configcache().reload();
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
        Bukkit.getPluginCommand("reloadsubplugin").setExecutor(new configcache());
        Bukkit.getPluginCommand("reloadsubplugin").setTabCompleter(new configcache());
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getWorld("world").setTime(6000);
        getServer().createWorld(new WorldCreator("inclined"));
        Bukkit.getWorld("inclined").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getWorld("inclined").setTime(6000);
        new ItemStackList().Load();
        LibEventManager.registerListeners(new ServerEvent());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                speedcubingServer.tcp.sendClean(25576 - Bukkit.getPort() % 2, "online|6|" + Bukkit.getOnlinePlayers().size());
            }
        }, 0, 1000);
    }
}
