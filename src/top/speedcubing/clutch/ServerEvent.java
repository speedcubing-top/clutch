package top.speedcubing.clutch;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.entity.Player;
import top.speedcubing.clutch.Core.NormalClutch;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.lib.bukkit.events.packet.PlayInEvent;
import top.speedcubing.server.events.ConfigReloadEvent;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.share.Chat;
import top.speedcubing.server.utils.config;

public class ServerEvent {
    @CubingEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.getPacket() instanceof PacketPlayInChat) {
            Player player = e.getPlayer();
            String message = ((PacketPlayInChat) e.getPacket()).a();
            Member member = Member.getMember(player);
            if (member.VeloSettings) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.sendLangMessage(GlobalString.integerOutOfRange, "0.000", "5.000");
                    else {
                        member.integerCache[player.getWorld().getName().equals(NormalClutch.map) ? 1 : 3] = x;
                        member.VeloSettings = false;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (message.charAt(0) != '/') {
                e.setCancelled(true);
                Chat.globalChat(player.getWorld().getPlayers(), player, GlobalString.MessageFormat, message, member.user.getPrefixName(false), member.user.getFormat(false).getChatColor());
            }
        }
    }

    @CubingEventHandler
    public void ToggleFlyEvent(ToggleFlyEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void NickEvent(NickEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void SkinEvent(SkinEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendLangMessage(GlobalString.OnlyInHub);
    }

    @CubingEventHandler
    public void ConfigReloadEvent(ConfigReloadEvent e) {
        JsonObject object = config.config.getAsJsonObject("clutchkb");
        Clutch.kb = new double[]{
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
}
