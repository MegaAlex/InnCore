/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.factionsmisc;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.FactionMiscConfig;
import me.megaalex.inncore.towns.TownsManager;
import me.megaalex.inncore.towns.object.Town;
import me.megaalex.inncore.utils.PlayerUtils;
import net.md_5.bungee.api.ChatColor;

public class MiningLevelManagerListener implements Listener {

    FactionMiscConfig config;
    int maxMiningLvl;

    public MiningLevelManagerListener(FactionMiscConfig config) {
        this.config = config;
        maxMiningLvl = 0 ;
        for(Map.Entry<Integer, String> entry : config.miningLevelNames.entrySet()) {
            if(entry.getKey() > maxMiningLvl) {
                maxMiningLvl = entry.getKey();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if(!e.getTo().getWorld().getName().equalsIgnoreCase("mining")) {
            return;
        }

        if(testingAssert(e.getPlayer())) {
            return;
        }

        if(processMoveCheck(e.getPlayer(), e.getFrom(), e.getTo())) {
            e.setCancelled(true);
        }
    }



    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!e.getTo().getWorld().getName().equalsIgnoreCase("mining")) {
            return;
        }

        if(testingAssert(e.getPlayer())) {
            return;
        }

        if(processMoveCheck(e.getPlayer(), e.getFrom(), e.getTo())) {
            e.setCancelled(true);
        }
    }

    private boolean processMoveCheck(Player player, Location from, Location to) {
        int px = getLevelX(from);
        int py = getLevelZ(from);

        int x = getLevelX(to);
        int y = getLevelZ(to);

        if(px == x && py == y) {
            return false;
        }

        // Zone change
        String newZone = getLvlName(x, y);
        Integer lvl = config.miningLevels.get(newZone);
        int highestLvl = getHighestLvlForPlayer(player);
        if(lvl == null) {
            if(highestLvl != -1) {
                player.sendMessage(ChatColor.RED + "You have reached the border of the mining world!");
                return true;
            }
            return false;
        }
        if(config.miningZoneTesting) {
            Bukkit.getLogger().info(highestLvl + " zonechecklvl " + lvl);
        }
        if(highestLvl < lvl && highestLvl != -1) {
            player.sendMessage(ChatColor.RED + "You can't enter into that mining zone!");
            return true;
        }

        return false;
    }

    private int getLevelX(Location checkLoc) {
        Location loc = checkLoc.clone().add(config.getSpawnX(), 0.0, config.getSpawnZ());
        return (int) Math.floor(loc.getX() / config.miningLevelSize);
    }

    private int getLevelZ(Location checkLoc) {
        Location loc = checkLoc.clone().add(config.getSpawnX(), 0.0, config.getSpawnZ());
        return (int) Math.floor(loc.getZ() / config.miningLevelSize);
    }

    public int getHighestLvlForPlayer(Player player) {
        int lvl = 1;
        if(player.hasPermission("inncore.mining.level.-1")) {
            return -1;
        }
        while(player.hasPermission("inncore.mining.level." + lvl ) && lvl <= maxMiningLvl) {
            lvl++;
        }
        TownsManager townsManager = InnCore.getInstance().getTownsManager();
        if(townsManager.isEnabled()) {
            Integer townZone = null;
            Town playerTown = townsManager.getDataManager().getPlayerTown(player.getName());
            if(playerTown != null) {
                townZone = config.townRankZone.get(playerTown.getRank());
            }

            lvl = (townZone != null && townZone >= lvl) ? townZone + 1 : lvl;
        }
        return lvl - 1;
    }

    private String getLvlName(int x, int y) {
        return x + ";" + y;
    }

    public void teleportPlayerToZone(Player player, int zoneId) {
        Location loc = getSpawnForZone(zoneId);
        loc.add(0.5, 1, 0.5);

        PlayerUtils.teleport(player, loc);
    }

    private Location getSpawnForZone(int zoneId) {
        World miningWorld = Bukkit.getWorld("mining");
        Location centerLoc = new Location(miningWorld, config.getSpawnX(), 1.0, config.getSpawnX());
        String miningLevel = config.miningLevelNames.get(zoneId);
        if(miningLevel == null) {
            return new Location(miningWorld, 0, miningWorld.getHighestBlockYAt(0, 0), 0);
        }
        String[] zoneInfoString = miningLevel.split(";");
        int x = Integer.parseInt(zoneInfoString[0]);
        int z = Integer.parseInt(zoneInfoString[1]);
        int zoneSize = config.miningLevelSize;
        int radius = zoneSize / 2;
        centerLoc.add(x * zoneSize + radius, 0, z * zoneSize + radius);
        centerLoc.setY(miningWorld.getHighestBlockYAt(centerLoc));
        return centerLoc;
    }


    public void setSpawnPoint(Player player) {
        config.setSpawnX(player.getLocation().getBlockX());
        config.setSpawnZ(player.getLocation().getBlockZ());
    }

    public boolean testingAssert(Player player) {
        return config.miningZoneTesting && !(player.getName().equalsIgnoreCase("MegaAlexch0")
                || player.getName().equalsIgnoreCase("ClockTime"));
    }
}
