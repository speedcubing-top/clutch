package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import top.speedcubing.clutch.Core.InclinedClutch;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.clutch.Member;
import top.speedcubing.server.lang.LangInv;
import top.speedcubing.server.lang.LangItem;

public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        switch (player.getItemInHand().getType()) {
            case INK_SACK: {
                Member member = Member.getMember(player);

                LangInv inventory = new LangInv(member.user, 27, "%lang_inv_settings%");

                boolean normal = player.getWorld().getName().equals(NormalClutch.map);

                inventory.setItem(new LangItem(Material.WATCH).name("%clutch_item_cooldown%").lore2("%lang_clutch_lore_cooldown%", String.valueOf(member.Delay / 20D)), 12);
                inventory.setItem(new LangItem(Material.SANDSTONE).name("%lang_item_blockskin%"), 13);
                inventory.setItem(new LangItem(Material.STICK).name("%lang_clutch_item_hits%").glow(), 10);
                inventory.setItem(new LangItem(Material.RAW_FISH).name("%lang_clutch_item_hitdelay%").durability(3).lore2("%lang_clutch_lore_hitdelay", String.valueOf(member.integerCache[normal ? 2 : 4])), 14);
                inventory.setItem(new LangItem(Material.LEATHER_BOOTS).name("%lang_clutch_item_velo%").durability(1).lore2("%lang_clutch_lore_velo%", String.format("%.3f", member.integerCache[normal ? 1 : 3] / 1000D)), 15);
                inventory.setItem(new LangItem(Material.GLOWSTONE_DUST).name("display hits in title").lore2("\n" + (member.display ? "click to turn off" : "click to turn on")), 16)
                ;

                member.user.openLangInventory(inventory);
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
