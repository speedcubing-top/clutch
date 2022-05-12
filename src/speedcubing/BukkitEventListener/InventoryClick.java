package speedcubing.BukkitEventListener;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.meta.ItemMeta;
import speedcubing.Core.InclinedClutch;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.Arrays;
import java.util.UUID;

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
                if (holder == ItemStackList.Settings) {
                    e.setCancelled(true);
                    if (RawSlot == 10) {
                        Member member = Member.getMember(player.getUniqueId());
                        boolean normal = player.getWorld().getName().equals("world");
                        Inventory inv = Bukkit.createInventory(normal ? NormalClutch.KB : InclinedClutch.KB, NormalClutch.hitcount, StringList.kbgui[member.lang]);
                        int length = normal ? NormalClutch.hitcount : InclinedClutch.hitcount;
                        int[] settings = normal ? member.KbCache : member.KbCache2;
                        ItemStack[] stacks = ItemStackList.settings[member.lang];
                        for (int i = 0; i < length; i++) {
                            inv.setItem(i, stacks[settings[i]]);
                            inv.getItem(i).setAmount(i + 1);
                        }
                        player.openInventory(inv);
                    } else if (RawSlot == 12) {
                        InventoryAction action = e.getAction();
                        switch (player.getWorld().getName()) {
                            case "world":
                                switch (action) {
                                    case PICKUP_ALL: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        Member member = Member.getMember(player.getUniqueId());
                                        int x = member.Delay;
                                        if (x != 20) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            member.Delay -= 2;
                                            member.reset();
                                            String[] str = StringList.LoreCoolDown[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x - 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.min[member.lang].replace("%int%", "1.0"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        Member member = Member.getMember(player.getUniqueId());
                                        int x = member.Delay;
                                        if (x != 400) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            member.Delay += 2;
                                            member.reset();
                                            String[] str = StringList.LoreCoolDown[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x + 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.max[member.lang].replace("%int%", "20.0"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                }
                                break;
                            case "inclined":
                                switch (action) {
                                    case PICKUP_ALL: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        Member member = Member.getMember(player.getUniqueId());
                                        int x = member.integerCache[2];
                                        if (x != 20) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.LoreCoolDown[member.lang];
                                            member.integerCache[2] -= 2;
                                            member.reset();
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x - 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.min[member.lang].replace("%int%", "1.0"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        UUID uuid = player.getUniqueId();
                                        Member member = Member.getMember(uuid);
                                        int x = member.integerCache[2];
                                        if (x != 400) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.LoreCoolDown[member.lang];
                                            member.integerCache[2] += 2;
                                            member.reset();
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x + 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.max[member.lang].replace("%int%", "20.0"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                }
                                break;
                        }
                    } else if (RawSlot == 13) {
                        player.sendMessage("Currently Unavailable to use.");
                    } else if (RawSlot == 14) {
                        InventoryAction action = e.getAction();
                        switch (player.getWorld().getName()) {
                            case "world":
                                switch (action) {
                                    case PICKUP_ALL: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        UUID uuid = player.getUniqueId();
                                        Member member = Member.getMember(uuid);
                                        int x = member.integerCache[2];
                                        if (x != 1) {
                                            member.integerCache[2] -= 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x - 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.min[member.lang].replace("%int%", "1"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        UUID uuid = player.getUniqueId();
                                        Member member = Member.getMember(uuid);
                                        int x = member.integerCache[2];
                                        if (x != 20) {
                                            member.integerCache[2] += 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x + 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.max[member.lang].replace("%int%", "20"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                }
                                break;
                            case "inclined":
                                switch (action) {
                                    case PICKUP_ALL: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        UUID uuid = player.getUniqueId();
                                        Member member = Member.getMember(uuid);
                                        int x = member.integerCache[4];
                                        if (x != 1) {
                                            member.integerCache[4] -= 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x - 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.min[member.lang].replace("%int%", "1"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                                        UUID uuid = player.getUniqueId();
                                        Member member = Member.getMember(uuid);
                                        int x = member.integerCache[4];
                                        if (x != 20) {
                                            member.integerCache[4] += 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x + 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            player.sendMessage(GlobalString.max[member.lang].replace("%int%", "20"));
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                                        }
                                    }
                                    break;
                                }
                                break;
                        }
                    } else if (RawSlot == 15) {
                        UUID uuid = player.getUniqueId();
                        player.closeInventory();
                        player.sendMessage(GlobalString.inputInteger[User.getUser(uuid).lang].replace("%min%", "0.000").replace("%max%", "5.000"));
                        Main.VeloSettings.add(uuid);
                        speedcubingServer.node(true, uuid);
                    } else if (RawSlot == 16) {
                        UUID uuid = player.getUniqueId();
                        if (PlayerInteract.display.contains(uuid)) {
                            PlayerInteract.display.remove(uuid);
                        } else PlayerInteract.display.add(uuid);
                        ItemStack stack = inventory.getItem(RawSlot);
                        ItemMeta meta = stack.getItemMeta();
                        meta.setLore(Arrays.asList(" ", PlayerInteract.display.contains(uuid) ? "click to turn off" : "click to turn on"));
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                        stack.setItemMeta(meta);
                    }
                } else if (holder == NormalClutch.KB) {
                    e.setCancelled(true);
                    UUID uuid = player.getUniqueId();
                    Inventory clickedInventory = e.getClickedInventory();
                    Member member = Member.getMember(uuid);
                    int[] settings = member.KbCache;
                    ItemStack[] hits = ItemStackList.settings[member.lang];
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
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                    }
                } else if (holder == InclinedClutch.KB) {
                    e.setCancelled(true);
                    UUID uuid = player.getUniqueId();
                    Inventory clickedInventory = e.getClickedInventory();
                    Member member = Member.getMember(uuid);
                    int[] settings = member.KbCache2;
                    ItemStack[] hits = ItemStackList.settings[member.lang];
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
                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                    }
                } else
                    player.sendMessage("Currently Unavailable to use.");
            }
        }
    }
}