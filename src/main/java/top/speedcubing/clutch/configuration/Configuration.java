package top.speedcubing.clutch.configuration;

import com.google.gson.JsonObject;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;

public class Configuration {

    public static double[] kb;

    public static void reload() {
        JsonObject object = ServerConfig.getConfig().getAsJsonObject("clutchkb");
        kb = new double[]{
                object.get("low").getAsJsonArray().get(0).getAsDouble(),
                object.get("low").getAsJsonArray().get(1).getAsDouble(),
                object.get("mid").getAsJsonArray().get(0).getAsDouble(),
                object.get("mid").getAsJsonArray().get(1).getAsDouble(),
                object.get("diff").getAsJsonArray().get(0).getAsDouble(),
                object.get("diff").getAsJsonArray().get(1).getAsDouble(),
                object.get("high").getAsJsonArray().get(0).getAsDouble(),
                object.get("high").getAsJsonArray().get(1).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(0).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(1).getAsDouble(),
                object.get("low").getAsJsonArray().get(2).getAsDouble(),
                object.get("low").getAsJsonArray().get(3).getAsDouble(),
                object.get("mid").getAsJsonArray().get(2).getAsDouble(),
                object.get("mid").getAsJsonArray().get(3).getAsDouble(),
                object.get("diff").getAsJsonArray().get(2).getAsDouble(),
                object.get("diff").getAsJsonArray().get(3).getAsDouble(),
                object.get("high").getAsJsonArray().get(2).getAsDouble(),
                object.get("high").getAsJsonArray().get(3).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(2).getAsDouble(),
                object.get("extreme").getAsJsonArray().get(3).getAsDouble()
        };
    }

    @CubingEventHandler(priority = 30)
    public void configReloadEvent(ConfigReloadEvent e) {
        reload();
    }
}
