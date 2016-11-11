/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.MiscConfig;
import me.megaalex.inncore.utils.PlayerUtils;

public class MiscListener implements Listener {

    private final MiscConfig config;

    public MiscListener(MiscConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        final Player player = e.getPlayer();
        if(config.tpOnJoin()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerUtils.teleport(player, InnCore.getInstance().getMiscManager().getSpawnLocation());
                }
            }.runTask(InnCore.getInstance());
        }

        if(config.disableCollision()) {
            InnCore.getInstance().getMiscManager().addPlayerToCollisionTeam(player);
            player.setCollidable(false);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);

        if(config.disableCollision()) {
            InnCore.getInstance().getMiscManager().removePlayerFromCollisionTeam(e.getPlayer());
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {

        if(!config.colorSigns()) {
            return;
        }

        for(int i = 0; i < e.getLines().length; i++)  {
            e.setLine(i, color(e.getLine(i)));
        }
    }

    @EventHandler
    public void onBookEdit(PlayerEditBookEvent e) {
        if(!config.colorBooks()) {
            return;
        }
        BookMeta data = e.getNewBookMeta();
        if(data.hasAuthor()) {
            data.setAuthor(color(data.getAuthor()));
        }
        if(data.hasTitle()) {
            data.setTitle(color(data.getTitle()));
        }

        List<String> pages = data.getPages();

        if(pages != null) {
            List<String> newPages = new ArrayList<>();
            for (String page : pages) {
                newPages.add(color(page));
            }
            data.setPages(newPages);
        }
        e.setNewBookMeta(data);
    }

    private String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @EventHandler
    public void onPlayerFall(PlayerMoveEvent e) {
        if(e.getTo().getY() <= 4.0 && config.tpOnFall()
                && config.getFallWorlds().contains(e.getTo().getWorld().getName().toLowerCase())) {
            PlayerUtils.teleport(e.getPlayer(), InnCore.getInstance().getMiscManager().getSpawnLocation());
        }
    }
}
