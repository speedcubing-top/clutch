package speedcubing.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.Core.NormalClutch;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.SideBar;

import java.util.UUID;

public class PlayerMove implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            World world = player.getWorld();
            switch (world.getName()) {
                case "world": {
                    UUID uuid = player.getUniqueId();
                    Member member = Member.getMember(uuid);
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getZ() - member.PlayerCoord - 0.5) > 5.5) {
                        String[] sidebar = StringList.SideBarNormal[member.user.lang];
                        SideBar.getSidebar(player)
                                .setLine(sidebar[4] + 0, 4)
                                .setLine(sidebar[1] + 0, 7);
                        member.Length = 0;
                        member.reset2(player, world);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, 0.5, 100, member.PlayerCoord + 0.5, 90, 0));
                    }
                }
                break;
                case "inclined":
                    Member member = Member.getMember(player.getUniqueId());
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getX() + to.getZ() - 2 * member.PlayerCoord - 1) > 9.5) {
                        String[] sidebar = StringList.SideBarInclined[member.user.lang];
                        SideBar.getSidebar(player)
                                .setLine(sidebar[6] + 0, 4)
                                .setLine(sidebar[5] + 0, 5)
                                .setLine(sidebar[4] + 0, 6)
                                .setLine(sidebar[1] + 0, 9);
                        member.Length2 = new int[]{member.PlayerCoord, member.PlayerCoord};
                        member.reset2(player, world);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, member.PlayerCoord + 0.5, 100, member.PlayerCoord + 0.5, 45, 0));
                    }
                    break;
            }
        }
    }
}
