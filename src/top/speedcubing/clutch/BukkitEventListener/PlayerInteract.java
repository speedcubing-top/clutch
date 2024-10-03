package top.speedcubing.clutch.BukkitEventListener;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.speedcubing.clutch.Core.InclinedClutch;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.clutch.List.ItemStackList;
import top.speedcubing.clutch.Member;
import top.speedcubing.lib.bukkit.events.inventory.ClickInventoryEvent;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
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

                inventory.setItem(new LangItem(Material.WATCH).name("%lang_clutch_item_cooldown%").lore2("%lang_clutch_lore_cooldown%", String.valueOf(member.Delay / 20D)),
                        PlayerInteract::settings12, 12);

                inventory.setItem(new LangItem(Material.SANDSTONE).name("%lang_item_blockskin%"), 13);

                inventory.setItem(new LangItem(Material.STICK).name("%lang_clutch_item_hits%").glow(), (r) -> {
                    Inventory inv = Bukkit.createInventory(normal ? NormalClutch.KB : InclinedClutch.KB, NormalClutch.hitcount + 9, "§bHits");
                    int length = normal ? NormalClutch.hitcount : InclinedClutch.hitcount;
                    int[] settings = normal ? member.KbCache : member.KbCache2;
                    ItemStack[] stacks = ItemStackList.settings[member.user.lang];
                    for (int i = 0; i < length; i++) {
                        inv.setItem(i, stacks[settings[i]]);
                        inv.getItem(i).setAmount(i + 1);
                    }
                    inv.setItem(NormalClutch.hitcount + 8, new ItemBuilder(Material.ARROW).name("next page").build());
                    player.openInventory(inv);
                }, 10);

                inventory.setItem(new LangItem(Material.RAW_FISH).name("%lang_clutch_item_hitdelay%").durability(3).lore2("%lang_clutch_lore_hitdelay%", String.valueOf(member.integerCache[normal ? 2 : 4])),
                        PlayerInteract::settings14, 14);

                inventory.setItem(new LangItem(Material.LEATHER_BOOTS).name("%lang_clutch_item_velo%").durability(1).lore2("%lang_clutch_lore_velo%", String.format("%.3f", member.integerCache[normal ? 1 : 3] / 1000D)), (r) -> {
                    player.closeInventory();
                    member.user.sendMessage("%lang_input_input_num%", "0.000", "5.000");
                    member.VeloSettings = true;
                    member.user.setInput(true);
                }, 15);

                inventory.setItem(new LangItem(Material.GLOWSTONE_DUST).name("display hits in title").lore2("\n" + (member.display ? "click to turn off" : "click to turn on")), (r) -> {
                    Inventory inv = r.getEvent().getInventory();
                    member.display = !member.display;
                    ItemStack stack = inv.getItem(r.getEvent().getRawSlot());
                    ItemMeta meta = stack.getItemMeta();
                    meta.setLore(Arrays.asList(" ", member.display ? "click to turn off" : "click to turn on"));
                    member.user.sound(Sound.NOTE_PLING);
                    stack.setItemMeta(meta);
                }, 16);

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

    public static void settings14(ClickInventoryEvent r) {
        InventoryClickEvent e = r.getEvent();
        Player player = r.getPlayer();
        int rawSlot = e.getRawSlot();
        Inventory inventory = e.getInventory();
        InventoryAction action = e.getAction();

        int index = player.getWorld().getName().equals(NormalClutch.map) ? 2 : 4;
        switch (action) {
            case PICKUP_ALL: {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = member.integerCache[index];
                if (x != 1) {
                    member.integerCache[index] -= 1;
                    member.user.setLangLore(inventory, rawSlot, "%lang_clutch_lore_hitdelay%", Integer.toString(x - 1));
                } else {
                    member.user.sendMessage("%lang_input_min%", "1.0");
                    member.user.sound(Sound.NOTE_PLING);
                }
            }
            break;
            case PICKUP_HALF: {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = member.integerCache[index];
                if (x != 20) {
                    member.integerCache[index] += 1;
                    member.user.setLangLore(inventory, rawSlot, "%lang_clutch_lore_hitdelay%", Integer.toString(x + 1));
                } else {
                    member.user.sendMessage("%lang_input_max%", "20.0");
                    member.user.sound(Sound.NOTE_PLING);
                }
            }
            break;
        }
    }

    public static void settings12(ClickInventoryEvent r) {
        InventoryClickEvent e = r.getEvent();
        Player player = r.getPlayer();
        int rawSlot = e.getRawSlot();
        Inventory inventory = e.getInventory();
        InventoryAction action = e.getAction();

        switch (action) {
            case PICKUP_ALL: {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = member.Delay;
                if (x != 20) {
                    member.Delay -= 2;
                    member.reset();
                    member.user.setLangLore(inventory, rawSlot, "%lang_clutch_lore_cooldown%", Double.toString((x - 2) / 20D));
                } else {
                    member.user.sendMessage("%lang_input_min%", "1.0");
                    member.user.sound(Sound.NOTE_PLING);
                }
            }
            break;
            case PICKUP_HALF: {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = member.Delay;
                if (x != 400) {
                    member.Delay += 2;
                    member.reset();
                    member.user.setLangLore(inventory, rawSlot, "%lang_clutch_lore_cooldown%", Double.toString((x + 2) / 20D));
                } else {
                    member.user.sendMessage("%lang_input_max%", "20.0");
                    member.user.sound(Sound.NOTE_PLING);
                }
            }
            break;
        }
    }
}
