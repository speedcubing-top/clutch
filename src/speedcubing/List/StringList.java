package speedcubing.List;

import speedcubing.server.config;

public class StringList {

    public static String[][] LoreCoolDown = {
            {"§bCoolDown Time", "", "§dCurrent Delay : §e%double%", "§aLeft: ↓§8 , §cRight: ↑"},
            {"§bCoolDown Time", "", "§dCurrent Delay : §e%double%", "§aLeft: ↓§8 , §cRight: ↑"},
            {"§bCoolDown Time", "", "§dCurrent Delay : §e%double%", "§aLeft: ↓§8 , §cRight: ↑"},
            {"§bCoolDown Time", "", "§dCurrent Delay : §e%double%", "§aLeft: ↓§8 , §cRight: ↑"}
    };

    public static String[][] SideBarInclined = {
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", " §aX §8» §b", " §aZ §8» §b", " §aHorizontal §8» §b", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", " §aX §8» §b", " §aZ §8» §b", " §aHorizontal §8» §b", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", " §aX §8» §b", " §aZ §8» §b", " §aHorizontal §8» §b", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", " §aX §8» §b", " §aZ §8» §b", " §aHorizontal §8» §b", "§3", "§aHits:", "§1§8»§e ", "§0"}
    };
    public static String[][] SideBarNormal = {
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", "§8»§b ", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", "§8»§b ", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", "§8»§b ", "§3", "§aHits:", "§1§8»§e ", "§0"},
            {"§3Clutch", "§6" + config.SERVERIP, "§6", "§aDistance:", "§8»§b ", "§3", "§aHits:", "§1§8»§e ", "§0"}
    };

    public static final String[] settingstitle = {"§cSettings", "§cSettings", "§cSettings", "§cSettings"};

    public static final String[] blockedit = {"§bBlock Skin", "§bBlock Skin", "§bBlock Skin", "§bBlock Skin"};

    public static final String[] kb = {"§bHits", "§bHits", "§bHits", "§bHits"};

    public static final String[] kbgui = {"§bHits", "§bHits", "§bHits", "§bHits"};

    public static String[][] currentVelo = {
            {"§bVertical Kb Multiplier", "", "§dCurrent:§e %kb%", "", "§cdefault: 1.000"},
            {"§bVertical Kb Multiplier", "", "§dCurrent:§e %kb%", "", "§cdefault: 1.000"},
            {"§bVertical Kb Multiplier", "", "§dCurrent:§e %kb%", "", "§cdefault: 1.000"},
            {"§bVertical Kb Multiplier", "", "§dCurrent:§e %kb%", "", "§cdefault: 1.000"}
    };

    public static String[][] HitDelaylore = {
            {"§bHit Delay", "", "§dCurrent: §e%int%§d tick(s)", "§aLeft: ↓§8 , §cRight: ↑", "", "§cdefault: 10"},
            {"§bHit Delay", "", "§dCurrent: §e%int%§d tick(s)", "§aLeft: ↓§8 , §cRight: ↑", "", "§cdefault: 10"},
            {"§bHit Delay", "", "§dCurrent: §e%int%§d tick(s)", "§aLeft: ↓§8 , §cRight: ↑", "", "§cdefault: 10"},
            {"§bHit Delay", "", "§dCurrent: §e%int%§d tick(s)", "§aLeft: ↓§8 , §cRight: ↑", "", "§cdefault: 10"}
    };
}
