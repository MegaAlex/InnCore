/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.sky;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.sky.data.FallDeathEntry;

public class PlayerFallListener implements Listener {

    SkyBlockManager manager;

    public PlayerFallListener(SkyBlockManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFallDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if(player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.VOID
                || !manager.getConfig().saveInvOnFallDeath()) {
            return;
        }

        final FallDeathEntry entry = new FallDeathEntry(player.getName(), Arrays.asList(player.getInventory().getContents()));
        new BukkitRunnable() {

            @Override
            public void run() {
                manager.getSql().insertEntry(entry);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }
}
