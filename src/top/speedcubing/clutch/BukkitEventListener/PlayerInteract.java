package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import top.speedcubing.clutch.Core.InclinedClutch;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.clutch.List.ItemStackList;
import top.speedcubing.clutch.List.StringList;
import top.speedcubing.clutch.Member;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        switch (player.getItemInHand().getType()) {
            case INK_SACK: {
                Member member = Member.getMember(player);
                Inventory inventory = Bukkit.createInventory(ItemStackList.Settings, 27, StringList.settingstitle.get(member.user.lang));
                String[] cooldown = StringList.LoreCoolDown[member.user.lang];
                String[] hit = StringList.HitDelaylore[member.user.lang];
                String[] velo = StringList.currentVelo[member.user.lang];
                boolean normal = player.getWorld().getName().equals(NormalClutch.map);
                inventory.setItem(12, new ItemBuilder(Material.WATCH).name(cooldown[0]).lore(cooldown[1], cooldown[2].replace("%double%", Double.toString(member.Delay / 20D)), cooldown[3]).build());
                inventory.setItem(13, new ItemBuilder(Material.SANDSTONE).name(StringList.blockedit.get(member.user.lang)).build());
                inventory.setItem(10, new ItemBuilder(Material.STICK).name(StringList.kb.get(member.user.lang)).glow().build());
                inventory.setItem(14, new ItemBuilder(Material.RAW_FISH).name(hit[0]).durability(3).lore(hit[1], hit[2].replace("%int%", Integer.toString(member.integerCache[normal ? 2 : 4])), hit[3], hit[4], hit[5]).build());
                inventory.setItem(15, new ItemBuilder(Material.LEATHER_BOOTS).name(velo[0]).durability(1).lore(velo[1], velo[2].replace("%kb%", String.format("%.3f", member.integerCache[normal ? 1 : 3] / 1000D)), velo[3], velo[4]).build());
                inventory.setItem(16, new ItemBuilder(Material.GLOWSTONE_DUST).name("display hits in title").lore(" ", member.display ? "click to turn off" : "click to turn on").build());
                player.openInventory(inventory);
            }
            break;
            case COMPASS: {
                if (player.getWorld().getName().equals(NormalClutch.map))
                    InclinedClutch.Join(player);
                else NormalClutch.Join(player, false);
            }
            break;
        }
    }
}
