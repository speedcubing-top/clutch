package top.speedcubing.clutch.List;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

public class ItemStackList {
    public static final InventoryHolder Settings = () -> null;
    public static final ItemStack[][] settings = new ItemStack[4][6];
    public static final ItemStack[][] join = new ItemStack[4][2];
    public static final ItemStack[] itemstacks = new ItemStack[4];
    public static final ItemStack Blocks = new ItemBuilder(Material.SANDSTONE).amount(64).build();

    public static final String[] blockedit = {"§bBlock Skin", "§bBlock Skin", "§bBlock Skin", "§bBlock Skin"};

    public void Load() {
        String[] display = {"§cpause & settings", "§cpause & settings", "§cpause & settings", "§cpause & settings"};
        String[] extreme = {"§5Extreme", "§5Extreme", "§5Extreme", "§5Extreme"};
        String[] hard = {"§cHard", "§cHard", "§cHard", "§cHard"};
        String[] difficult = {"§6Difficult", "§6Difficult", "§6Difficult", "§6Difficult"};
        String[] normal = {"§eNormal", "§eNormal", "§eNormal", "§eNormal"};
        String[] easy = {"§aEasy", "§aEasy", "§aEasy", "§aEasy"};
        String[] none = {"§7None", "§7None", "§7None", "§7None"};
        String[] goinclined = {"§ago to Inclined Mode", "§ago to Inclined Mode", "§ago to Inclined Mode", "§ago to Inclined Mode"};
        String[] gostraight = {"§ago to Normal Mode", "§ago to Normal Mode", "§ago to Normal Mode", "§ago to Normal Mode"};

        for (int i = 0; i < 4; i++) {
            settings[i][0] =new ItemBuilder(Material.STAINED_GLASS).durability(8).name(none[i]).build();
            settings[i][1] = new ItemBuilder(Material.STAINED_GLASS).durability(5).name(easy[i]).build();
            settings[i][2] = new ItemBuilder(Material.STAINED_GLASS).durability(4).name(normal[i]).build();
            settings[i][3] = new ItemBuilder(Material.STAINED_GLASS).durability(1).name(difficult[i]).build();
            settings[i][4] = new ItemBuilder(Material.STAINED_GLASS).durability(14).name(hard[i]).build();
            settings[i][5] = new ItemBuilder(Material.STAINED_GLASS).durability(10).name(extreme[i]).build();
            join[i][0] =new ItemBuilder(Material.COMPASS).name(gostraight[i]).build();
            join[i][1] =new ItemBuilder(Material.COMPASS).name(goinclined[i]).build();
            itemstacks[i] = new ItemBuilder(Material.INK_SACK).durability(1).name(display[i]).build();
        }
    }
}
