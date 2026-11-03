package top.speedcubing.clutch.list;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;

public class ItemStackList {
    public static final ItemStack[][] settings = new ItemStack[4][6];
    public static final ItemStack[][] join = new ItemStack[4][2];
    public static final ItemStack[] itemstacks = new ItemStack[4];
    public static final ItemStack Blocks = new ItemBuilder(Material.SANDSTONE).amount(64).build();

     public void Load() {
        String[] display = StringList.lang("clutch_item_pause_settings");
        String[] extreme = StringList.lang("clutch_difficulty_extreme");
        String[] hard = StringList.lang("clutch_difficulty_hard");
        String[] difficult = StringList.lang("clutch_difficulty_difficult");
        String[] normal = StringList.lang("clutch_difficulty_normal");
        String[] easy = StringList.lang("clutch_difficulty_easy");
        String[] none = StringList.lang("clutch_difficulty_none");
        String[] goinclined = StringList.lang("clutch_item_go_inclined");
        String[] gostraight = StringList.lang("clutch_item_go_normal");

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
