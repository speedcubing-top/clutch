package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.clutch.Core.InclinedClutch;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.clutch.List.ItemStackList;
import top.speedcubing.clutch.Member;

public class InventoryClick implements Listener {
    @EventHandler
    public void InvetoryClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int RawSlot = e.getRawSlot();
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            if (inventory.getName().equals("container.crafting")) {
                if (player.getGameMode() != GameMode.CREATIVE)
                    e.setCancelled(true);
            } else {
                InventoryHolder holder = inventory.getHolder();
                if (holder == NormalClutch.KB) {
                    e.setCancelled(true);
                    Inventory clickedInventory = e.getClickedInventory();
                    Member member = Member.getMember(player);
                    int[] settings = member.KbCache;

                    if (RawSlot > 45) { //page setting

                        return;
                    }
                    ItemStack[] hits = ItemStackList.settings[member.user.lang];
                    if (RawSlot >= 0 && RawSlot < NormalClutch.hitcount) {
                        if (RawSlot != 0 && settings[RawSlot - 1] == 0) {
                            for (int i = RawSlot; i >= 0; i--) {
                                if (settings[i] == 0) {
                                    settings[i] = 1;
                                    inventory.setItem(i, hits[1]);
                                    inventory.getItem(i).setAmount(i + 1);
                                } else break;
                            }
                        } else {
                            if (settings[RawSlot] != 5) {
                                settings[RawSlot] += 1;
                                inventory.setItem(RawSlot, hits[settings[RawSlot]]);
                            } else if (RawSlot == NormalClutch.hitcount - 1) {
                                settings[RawSlot] = 0;
                                inventory.setItem(RawSlot, hits[0]);
                            } else if (settings[RawSlot + 1] != 0) {
                                if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                                    settings[RawSlot] = 1;
                                    inventory.setItem(RawSlot, hits[1]);
                                } else {
                                    ItemStack x = hits[0];
                                    for (int i = RawSlot; i < NormalClutch.hitcount; i++) {
                                        if (settings[i] != 0) {
                                            settings[i] = 0;
                                            clickedInventory.setItem(i, x);
                                            clickedInventory.getItem(i).setAmount(i + 1);
                                        } else break;
                                    }
                                }
                            } else {
                                settings[RawSlot] = 0;
                                clickedInventory.setItem(RawSlot, hits[0]);
                            }
                            inventory.getItem(RawSlot).setAmount(RawSlot + 1);
                        }
                        member.user.sound(Sound.CLICK);
                    }
                } else if (holder == InclinedClutch.KB) {
                    e.setCancelled(true);
                    Inventory clickedInventory = e.getClickedInventory();
                    Member member = Member.getMember(player);
                    int[] settings = member.KbCache2;
                    ItemStack[] hits = ItemStackList.settings[member.user.lang];
                    if (RawSlot >= 0 && RawSlot < InclinedClutch.hitcount) {
                        if (RawSlot != 0 && settings[RawSlot - 1] == 0) {
                            for (int i = RawSlot; i >= 0; i--) {
                                if (settings[i] == 0) {
                                    settings[i] = 1;
                                    inventory.setItem(i, hits[1]);
                                    inventory.getItem(i).setAmount(i + 1);
                                } else break;
                            }
                        } else {
                            if (settings[RawSlot] != 5) {
                                settings[RawSlot] += 1;
                                inventory.setItem(RawSlot, hits[settings[RawSlot]]);
                            } else if (RawSlot == InclinedClutch.hitcount - 1) {
                                settings[RawSlot] = 0;
                                inventory.setItem(RawSlot, hits[0]);
                            } else if (settings[RawSlot + 1] != 0) {
                                if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                                    settings[RawSlot] = 1;
                                    inventory.setItem(RawSlot, hits[1]);
                                } else {
                                    ItemStack x = hits[0];
                                    for (int i = RawSlot; i < InclinedClutch.hitcount; i++) {
                                        if (settings[i] != 0) {
                                            settings[i] = 0;
                                            clickedInventory.setItem(i, x);
                                            clickedInventory.getItem(i).setAmount(i + 1);
                                        } else break;
                                    }
                                }
                            } else {
                                settings[RawSlot] = 0;
                                clickedInventory.setItem(RawSlot, hits[0]);
                            }
                            inventory.getItem(RawSlot).setAmount(RawSlot + 1);
                        }
                        member.user.sound(Sound.CLICK);
                    }
                }
            }
        }
    }
}