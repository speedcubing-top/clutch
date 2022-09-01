package top.speedcubing.clutch.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import top.speedcubing.clutch.List.StringList;
import top.speedcubing.clutch.Main;
import top.speedcubing.clutch.Member;
import top.speedcubing.lib.bukkit.SideBar;

public class PlayerMove implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            World world = player.getWorld();
            switch (world.getName()) {
                case "world": {
                    Member member = Member.getMember(player);
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getZ() - member.PlayerCoord - 0.5) > 5.5) {
                        String[] sidebar = StringList.SideBarNormal[member.user.lang];
                        SideBar.getSidebar(player)
                                .setLine(sidebar[4] + 0, 4)
                                .setLine(sidebar[7] + 0, 7);
                        member.Length = 0;
                        member.reset2(player, world);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> player.teleport(new Location(player.getWorld(), 0.5, 100, member.PlayerCoord + 0.5, 90, 0)));
                    }
                }
                break;
                case "inclined":
                    Member member = Member.getMember(player);
                    Location to = e.getTo();
                    if (to.getY() <= 96 || Math.abs(to.getX() + to.getZ() - 2 * member.PlayerCoord - 1) > 9.5) {
                        String[] sidebar = StringList.SideBarInclined[member.user.lang];
                        SideBar.getSidebar(player)
                                .setLine(sidebar[4] + 0, 4)
                                .setLine(sidebar[5] + 0, 5)
                                .setLine(sidebar[6] + 0, 6)
                                .setLine(sidebar[9] + 0, 9);
                        member.Length2 = new int[]{member.PlayerCoord, member.PlayerCoord};
                        member.reset2(player, world);
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> player.teleport(new Location(player.getWorld(), member.PlayerCoord + 0.5, 100, member.PlayerCoord + 0.5, 45, 0)));
                    }
                    break;
            }
        }
    }
}
