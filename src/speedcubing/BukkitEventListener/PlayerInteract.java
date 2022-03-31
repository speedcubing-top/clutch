package speedcubing.BukkitEventListener;

import cubing.lib.bukkit.ItemBuilder;
import cubingserver.libs.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import speedcubing.Core.Core;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerInteract implements Listener {

    public static Set<UUID> display = new HashSet<>();

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        switch (player.getItemInHand().getType()) {
            case INK_SACK: {
                UUID uuid = player.getUniqueId();
                int lang = User.getLang(uuid);
                Inventory inventory = Bukkit.createInventory(ItemStackList.Settings, 27, StringList.settingstitle[lang]);
                String[] cooldown = StringList.LoreCoolDown[lang];
                String[] hit = StringList.HitDelaylore[lang];
                String[] velo = StringList.currentVelo[lang];
                Integer[] x = Core.integerCache.get(uuid);
                boolean normal = player.getWorld().getName().equals("world");
                inventory.setItem(12, new ItemBuilder(Material.WATCH).name(cooldown[0]).lore(cooldown[1], cooldown[2].replace("%double%", Double.toString(Core.Delay.get(uuid) / 20D)), cooldown[3]).build());
                inventory.setItem(13, new ItemBuilder(Material.SANDSTONE).name(StringList.blockedit[lang]).build());
                inventory.setItem(10, new ItemBuilder(Material.STICK).name(StringList.kb[lang]).glow().build());
                inventory.setItem(14, new ItemBuilder(Material.RAW_FISH).name(hit[0]).durability(3).lore(hit[1], hit[2].replace("%int%", Integer.toString(x[normal ? 2 : 4])), hit[3], hit[4], hit[5]).build());
                inventory.setItem(15, new ItemBuilder(Material.LEATHER_BOOTS).name(velo[0]).durability(1).lore(velo[1], velo[2].replace("%kb%", String.format("%.3f", x[normal ? 1 : 3] / 1000D)), velo[3], velo[4]).build());
                inventory.setItem(16, new ItemBuilder(Material.GLOWSTONE_DUST).name("display hits in title").lore(" ", display.contains(uuid) ? "click to turn off" : "click to turn on").build());
                player.openInventory(inventory);
            }
            break;
            case COMPASS: {
                if (player.getWorld().getName().equals("world"))
                    InclinedClutch.Join(player);
                else NormalClutch.Join(player, false);
            }
            break;
        }
    }
}
