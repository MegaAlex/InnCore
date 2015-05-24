/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.factionsmisc;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.FactionMiscConfig;
import me.megaalex.inncore.utils.PlayerUtils;

public class FactionMiscListener implements Listener {

    private final FactionMiscConfig config;

    public FactionMiscListener(FactionMiscConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!config.miningEnable) {
            return;
        }
        if(e.getTo().getWorld().getName().equalsIgnoreCase("mining") && !e.getPlayer().hasPermission(config.miningPerm)) {
            e.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to enter the mining world! " +
                    "Select a race first.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent e) {
        if(!config.disableStrPotions) {
            return;
        }
        if(e.getItem().getType() != Material.POTION) {
            return;
        }
        Potion potion = Potion.fromItemStack(e.getItem());
        for(PotionEffect potionEffect : potion.getEffects()) {
            if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potionEffect.getAmplifier() >= 1) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Strength II potions are disabled!");
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionThrow(PotionSplashEvent e) {
        if(!config.disableStrPotions) {
            return;
        }
        ThrownPotion potion = e.getPotion();
        for(PotionEffect potionEffect : potion.getEffects()) {
            if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potionEffect.getAmplifier() >= 1) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!config.disableNaturalObsidian || e.getBlock().getType() != Material.OBSIDIAN) {
            return;
        }

        List<MetadataValue> metaData = e.getBlock().getMetadata("innCoreObsidian");
        if(metaData == null || metaData.isEmpty()) {
            PlayerUtils.sendMessage(e.getPlayer(), config.obsidianNoticeMessage);
            e.getBlock().setType(Material.AIR);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if(!config.disableNaturalObsidian || e.getBlock().getType() != Material.OBSIDIAN) {
            return;
        }

        e.getBlock().setMetadata("innCoreObsidian", new FixedMetadataValue(InnCore.getInstance(), true));
    }
}
