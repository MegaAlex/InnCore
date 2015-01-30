package me.megaalex.inncore.pvp;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.database.Sql;
import me.megaalex.inncore.pvp.events.InnPvpKillEvent;
import me.megaalex.inncore.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        Bukkit.getScheduler().runTaskAsynchronously(InnCore.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                PvpManager.addKill(event.getKiller().getName());
                Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
                sql.addKillHistory(event.getKiller().getName(), event.getVictim().getName(),
                        event.getItem().name(), time);
            }
        });
    }

}
