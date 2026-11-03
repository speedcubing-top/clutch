package top.speedcubing.clutch.list;

import top.speedcubing.common.text.Msg;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.lang.LanguageSystem;

public class StringList {
    public static final String PREFIX = Msg.prefix("Clutch", 'e');

    public static String[][] SideBarInclined = new String[LanguageSystem.langCount][];
    public static String[][] SideBarNormal = new String[LanguageSystem.langCount][];
    public static final String[] GO = lang("clutch_go");

    static {
        for (int i = 0; i < LanguageSystem.langCount; i++) {
            String distance = Lang.of("%lang_clutch_sidebar_distance%").getString(i);
            String hits = Lang.of("%lang_clutch_sidebar_hits%").getString(i);
            String horizontal = Lang.of("%lang_clutch_sidebar_horizontal%").getString(i);
            SideBarInclined[i] = new String[]{"§3Clutch", "§6speedcubing.top", "§6", distance, " §aX §8» §b", " §aZ §8» §b", " " + horizontal + " §8» §b", "§3", hits, "§1§8»§e ", "§0"};
            SideBarNormal[i] = new String[]{"§3Clutch", "§6speedcubing.top", "§6", distance, "§8»§b ", "§3", hits, "§1§8»§e ", "§0"};
        }
    }

    public static String[] lang(String key) {
        String[] result = new String[LanguageSystem.langCount];
        for (int i = 0; i < result.length; i++)
            result[i] = Lang.of("%lang_" + key + "%").getString(i);
        return result;
    }
}
