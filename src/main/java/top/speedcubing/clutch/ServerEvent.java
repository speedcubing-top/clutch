package top.speedcubing.clutch;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.entity.Player;
import top.speedcubing.clutch.core.NormalClutch;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.rank.RankFormat;
import top.speedcubing.lib.bukkit.events.packet.PlayInEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.share.Chat;

public class ServerEvent {

    @CubingEventHandler
    public void configReloadEvent(ConfigReloadEvent e) {
        JsonObject object = ServerConfig.config.getAsJsonObject("clutchkb");
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

    @CubingEventHandler
    public void playInEvent(PlayInEvent e) {
        if (e.getPacket() instanceof PacketPlayInChat) {
            Player player = e.getPlayer();
            String message = ((PacketPlayInChat) e.getPacket()).a();
            Member member = Member.getMember(player);
            if (member.VeloSettings) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "5.000");
                    else {
                        member.integerCache.set(player.getWorld().getName().equals(NormalClutch.map) ? 1 : 3, x);
                        member.VeloSettings = false;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (message.charAt(0) != '/') {
                e.setCancelled(true);
                RankFormat format = member.user.getFormat(false);
                Chat.globalChat(player.getWorld().getPlayers(), player, Lang.of("%lang_message_format%"), message, format.getPrefix() + player.getName(), format.getChatColor());
            }
        }
    }


    @CubingEventHandler
    public void nickEvent(NickEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void skinEvent(SkinEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void toggleFlyEvent(ToggleFlyEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

}
