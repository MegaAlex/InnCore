package me.megaalex.inncore.pvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.pvp.events.InnPvpKillEvent;
import me.megaalex.inncore.utils.NumberUtils;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKill(PlayerDeathEvent event) {

        if(event.getEntity().getKiller() == null) {
            return;
        }

        Player player = event.getEntity();
        Player killer = player.getKiller();

        // Call the inn kill event
        InnPvpKillEvent innEvent = new InnPvpKillEvent(killer, player,
                player.getLocation(), killer.getItemInHand().getType());
        Bukkit.getPluginManager().callEvent(innEvent);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPvpKill(final InnPvpKillEvent event) {
        final long time = NumberUtils.getCurrentTime();
        final String killer = event.getKiller().getName();
        final String victim = event.getVictim().getName();
        final String itemName = event.getItem().name();
        new BukkitRunnable() {
            @Override
            public void run() {
                PvpManager.addKill(killer);
                PvpSqlModule sql = InnCore.getInstance().getPvpManager().getSql();
                sql.addKillHistory(killer, victim, itemName, time);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

}
