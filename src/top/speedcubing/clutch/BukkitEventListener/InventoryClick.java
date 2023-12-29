package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import top.speedcubing.clutch.Core.*;
import top.speedcubing.clutch.List.*;
import top.speedcubing.clutch.Member;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.speedcubingServer;

import java.util.Arrays;

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
                        Member member = Member.getMember(player);
                        boolean normal = player.getWorld().getName().equals(NormalClutch.map);
                        Inventory inv = Bukkit.createInventory(normal ? NormalClutch.KB : InclinedClutch.KB, NormalClutch.hitcount, StringList.kbgui.get(member.user.lang));
                        int length = normal ? NormalClutch.hitcount : InclinedClutch.hitcount;
                        int[] settings = normal ? member.KbCache : member.KbCache2;
                        ItemStack[] stacks = ItemStackList.settings[member.user.lang];
                        for (int i = 0; i < length; i++) {
                            inv.setItem(i, stacks[settings[i]]);
                            inv.getItem(i).setAmount(i + 1);
                        }
                        player.openInventory(inv);
                    } else if (RawSlot == 12) {
                        InventoryAction action = e.getAction();
                        switch (player.getWorld().getName()) {
                            case NormalClutch.map:
                                switch (action) {
                                    case PICKUP_ALL: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.Delay;
                                        if (x != 20) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            member.Delay -= 2;
                                            member.reset();
                                            String[] str = StringList.LoreCoolDown[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x - 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.min, "1.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.Delay;
                                        if (x != 400) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            member.Delay += 2;
                                            member.reset();
                                            String[] str = StringList.LoreCoolDown[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x + 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.max, "20.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                }
                                break;
                            case "inclined":
                                switch (action) {
                                    case PICKUP_ALL: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.Delay;
                                        if (x != 20) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.LoreCoolDown[member.user.lang];
                                            member.Delay -= 2;
                                            member.reset();
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x - 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.min, "1.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.Delay;
                                        if (x != 400) {
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.LoreCoolDown[member.user.lang];
                                            member.Delay += 2;
                                            member.reset();
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%double%", Double.toString((x + 2) / 20D)), str[3]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.max, "20.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                }
                                break;
                        }
                    } else if (RawSlot == 14) {
                        InventoryAction action = e.getAction();
                        switch (player.getWorld().getName()) {
                            case NormalClutch.map:
                                switch (action) {
                                    case PICKUP_ALL: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.integerCache[2];
                                        if (x != 1) {
                                            member.integerCache[2] -= 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x - 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.min, "1.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.integerCache[2];
                                        if (x != 20) {
                                            member.integerCache[2] += 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x + 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.max, "20.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                }
                                break;
                            case InclinedClutch.map:
                                switch (action) {
                                    case PICKUP_ALL: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.integerCache[4];
                                        if (x != 1) {
                                            member.integerCache[4] -= 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x - 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.min, "1.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                    case PICKUP_HALF: {
                                        Member member = Member.getMember(player);
                                        member.user.sound(Sound.CLICK);
                                        int x = member.integerCache[4];
                                        if (x != 20) {
                                            member.integerCache[4] += 1;
                                            ItemStack stack = inventory.getItem(RawSlot);
                                            ItemMeta meta = stack.getItemMeta();
                                            String[] str = StringList.HitDelaylore[member.user.lang];
                                            meta.setLore(Arrays.asList(str[1], str[2].replace("%int%", Integer.toString(x + 1)), str[3], str[4], str[5]));
                                            stack.setItemMeta(meta);
                                        } else {
                                            member.user.sendLangMessage(GlobalString.max, "20.0");
                                            member.user.sound(Sound.NOTE_PLING);
                                        }
                                    }
                                    break;
                                }
                                break;
                        }
                    } else if (RawSlot == 15) {
                        player.closeInventory();
                        Member member = Member.getMember(player);
                        member.user.sendLangMessage(GlobalString.inputInteger, "0.000", "5.000");
                        member.VeloSettings = true;
                        speedcubingServer.node(true, member.user.id, member.user.tcpPort);
                    } else if (RawSlot == 16) {
                        Member member = Member.getMember(player);
                        member.display = !member.display;
                        ItemStack stack = inventory.getItem(RawSlot);
                        ItemMeta meta = stack.getItemMeta();
                        meta.setLore(Arrays.asList(" ", member.display ? "click to turn off" : "click to turn on"));
                        member.user.sound(Sound.NOTE_PLING);
                        stack.setItemMeta(meta);
                    }
                } else if (holder == NormalClutch.KB) {
                    e.setCancelled(true);
                    Inventory clickedInventory = e.getClickedInventory();
                    Member member = Member.getMember(player);
                    int[] settings = member.KbCache;
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