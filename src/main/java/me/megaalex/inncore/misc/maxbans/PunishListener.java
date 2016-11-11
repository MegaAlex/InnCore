/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.misc.maxbans;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.maxgamer.maxbans.events.PunishEvent;

import me.megaalex.inncore.InnCore;

public class PunishListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPunish(final PunishEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                InnCore.getInstance().getMaxbansManager().getMaxbansSqlModule().saveEntry(e);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }
}
