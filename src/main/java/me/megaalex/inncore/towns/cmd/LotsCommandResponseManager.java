/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.towns.CommandResponse;
import me.megaalex.inncore.towns.TownsManager;
import me.megaalex.inncore.towns.object.Town;
import me.megaalex.inncore.towns.object.ValidationResult;
import me.megaalex.inncore.utils.WorldEditUtil;

public class LotsCommandResponseManager extends CommandResponse {


    String worldName = "world";
    public LotsCommandResponseManager(TownsManager townsManager) {
        super(townsManager);
        worldName = InnCore.getInstance().getConfigManager().getTownsConfig().getWorld();

    }

    public void define(CommandSender sender, String townName, String lot, String flags) {
        boolean curT = townName == null;

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this!");
            return;
        }

        ValidationResult result = validateMain(sender, null, townName, "inntown.lot.define", "inntown.admin.lot.define");
        if(!result.success) {
            return;
        }
        
        if(flags == null)
            flags = "";


        Player player = (Player) sender;
        Town town = result.town;

        if(curT && !town.isMayor(player.getName()) && !town.isDeputy(player.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }
        WorldGuardPlugin wg = WGBukkit.getPlugin();
        World world = Bukkit.getWorld(worldName);
        RegionManager regionManager = wg.getRegionManager(world);
        String lotName = "t" + town.getId() + "_" + lot;
        ProtectedRegion townRegion = regionManager.getRegion("t" + town.getId());
        if(townRegion == null) {
            msgManager.sendMessage(sender, "error_lot_no_town_protection");
            return;
        }

        Selection selection = WorldEditUtil.getSelection(player);
        if(selection == null) {
            msgManager.sendMessage(sender, "error_lot_no_selection");
            return;
        }
        if(!(selection instanceof CuboidSelection)) {
            msgManager.sendMessage(sender, "error_lot_selection_invalid");
            return;
        }

        if(regionManager.hasRegion(lotName)) {
            msgManager.sendMessage(sender, "error_lot_exists");
            return;
        }

        CuboidSelection cubSelection = (CuboidSelection) selection;
        ProtectedCuboidRegion lotRegion = getLotRegion(lotName, cubSelection, !flags.contains("n"));
        if(!townRegion.contains(BukkitUtil.toVector(cubSelection.getMinimumPoint()))
                || !townRegion.contains(BukkitUtil.toVector(cubSelection.getMaximumPoint()))) {
            msgManager.sendMessage(sender, "error_lot_outside_town_protection");
            return;
        }
        try {
            lotRegion.setParent(townRegion);
        } catch (ProtectedRegion.CircularInheritanceException e) {
            e.printStackTrace();
        }
        regionManager.addRegion(lotRegion);
        msgManager.sendMessage(sender, "lot_defined", lot);
        msgManager.sendMessage(sender, "lot_defined_info");
    }

    public void remove(CommandSender sender, String townName, String lot) {
        boolean curT = townName == null;

        ValidationResult result = validateMain(sender, null, townName, "inntown.lot.remove", "inntown.admin.lot.remove");
        if(!result.success) {
            return;
        }

        Town town = result.town;

        if(curT && !town.isMayor(sender.getName()) && !town.isDeputy(sender.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }

        LotValidationResult lotResult = validateLot(sender, town, lot);
        if(!lotResult.success) {
            return;
        }

        lotResult.regionManager.removeRegion(lotResult.lotName, RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
        msgManager.sendMessage(sender, "lot_removed", lot);
    }
    public void redefine(CommandSender sender, String townName, String lot, String flags) {
        boolean curT = townName == null;

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this!");
            return;
        }

        ValidationResult result = validateMain(sender, null, townName, "inntown.lot.redefine", "inntown.admin.lot.redefine");
        if(!result.success) {
            return;
        }
        
        if(flags == null)
            flags = "";


        Player player = (Player) sender;
        Town town = result.town;

        if(curT && !town.isMayor(player.getName()) && !town.isDeputy(player.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }
        LotValidationResult lotResult = validateLot(sender, town, lot);
        ProtectedRegion townRegion = lotResult.townRegion;
        if(!lotResult.success) {
            return;
        }

        Selection selection = WorldEditUtil.getSelection(player);
        if(selection == null) {
            msgManager.sendMessage(sender, "error_lot_no_selection");
            return;
        }
        if(!(selection instanceof CuboidSelection)) {
            msgManager.sendMessage(sender, "error_lot_selection_invalid");
            return;
        }

        CuboidSelection cubSelection = (CuboidSelection) selection;
        ProtectedCuboidRegion lotRegion = getLotRegion(lotResult.lotName, cubSelection, !flags.contains("n"));
        if(!townRegion.contains(BukkitUtil.toVector(cubSelection.getMinimumPoint()))
                || !townRegion.contains(BukkitUtil.toVector(cubSelection.getMaximumPoint()))) {
            msgManager.sendMessage(sender, "error_lot_outside_town_protection");
            return;
        }
        try {
            lotRegion.setParent(lotResult.townRegion);
        } catch (ProtectedRegion.CircularInheritanceException e) {
            e.printStackTrace();
        }
        lotResult.regionManager.addRegion(lotRegion);
        msgManager.sendMessage(sender, "lot_redefined", lot);
    }

    public void addOwner(CommandSender sender, String townName, String lot, String playerName) {
        boolean curT = townName == null;

        ValidationResult result = validateMain(sender, playerName, townName, "inntown.lot.addowner", "inntown.admin.lot.addowner");
        if(!result.success) {
            return;
        }

        Town town = result.town;
        if(curT && !town.isMayor(sender.getName()) && !town.isDeputy(sender.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }
        LotValidationResult lotResult = validateLot(sender, town, lot);
        if(!lotResult.success) {
            return;
        }

        if(!town.isInTown(playerName)) {
            msgManager.sendMessage(sender, "error_not_member", playerName);
            return;
        }
        lotResult.lotRegion.getOwners().addPlayer(result.offlinePlayer.getName());

        msgManager.sendMessage(sender, "lot_owner_added", result.offlinePlayer.getName(), lot);
        msgManager.sendMessage(sender, "lot_owner_addremove_info");
    }

    public void removeOwner(CommandSender sender, String townName, String lot, String playerName) {
        boolean curT = townName == null;

        ValidationResult result = validateMain(sender, playerName, townName, "inntown.lot.removeowner", "inntown.admin.lot.removeowner");
        if(!result.success) {
            return;
        }

        Town town = result.town;
        if(curT && !town.isMayor(sender.getName()) && !town.isDeputy(sender.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }
        LotValidationResult lotResult = validateLot(sender, town, lot);
        if(!lotResult.success) {
            return;
        }

        LocalPlayer localPlayer = WGBukkit.getPlugin().wrapOfflinePlayer(result.offlinePlayer);

        if(!lotResult.lotRegion.isOwner(localPlayer)) {
            msgManager.sendMessage(sender, "error_owner_remove_not_added:", playerName);
            return;
        }

        lotResult.lotRegion.getOwners().removePlayer(localPlayer);

        msgManager.sendMessage(sender, "lot_owner_removed", result.offlinePlayer.getName(), lot);
        msgManager.sendMessage(sender, "lot_owner_addremove_info");
    }

    public void clearPlayers(CommandSender sender, String townName, String lot) {
        boolean curT = townName == null;

        ValidationResult result = validateMain(sender, null, townName, "inntown.lot.clearplayers", "inntown.admin.lot.clearplayers");
        if(!result.success) {
            return;
        }

        Town town = result.town;
        if(curT && !town.isMayor(sender.getName()) && !town.isDeputy(sender.getName())) {
            msgManager.sendMessage(sender, "error_lot_notdeputy");
            return;
        }
        LotValidationResult lotResult = validateLot(sender, town, lot);
        if(!lotResult.success) {
            return;
        }
        ProtectedRegion lotRegion = lotResult.lotRegion;
        lotRegion.setOwners(new DefaultDomain());
        lotRegion.setMembers(new DefaultDomain());

        msgManager.sendMessage(sender, "lot_players_cleared", lot);
    }

    public void info(CommandSender sender, String townName, String lot) {
        boolean curT = townName == null;

        ValidationResult result = validateMain(sender, null, townName, "inntown.lot.info", "inntown.lot.info.others");
        if(!result.success) {
            return;
        }
        Town town = result.town;
        LotValidationResult lotResult = validateLot(sender, town, lot);
        if(!lotResult.success) {
            return;
        }
        Bukkit.getServer().dispatchCommand(sender, "rg info " + lotResult.lotName);
    }

    private ProtectedCuboidRegion getLotRegion(String lotName, CuboidSelection selection, boolean expand) {
        Location minLocation = selection.getMinimumPoint();
        Location maxLocation = selection.getMaximumPoint();
        if(expand) {
            minLocation.setY(0);
            maxLocation.setY(maxLocation.getWorld().getMaxHeight());
        }
        BlockVector minLotVector = BukkitUtil.toVector(minLocation.getBlock());
        BlockVector maxLotVector = BukkitUtil.toVector(maxLocation.getBlock());
        return new ProtectedCuboidRegion(lotName, minLotVector, maxLotVector);
    }

    private class LotValidationResult {
        boolean success;
        RegionManager regionManager;
        ProtectedRegion townRegion;
        ProtectedRegion lotRegion;
        String lotName;

        LotValidationResult(boolean success) {
            this.success = success;
            regionManager = null;
            townRegion = null;
            lotRegion = null;
            lotName = null;
        }

        LotValidationResult(RegionManager regionManager, ProtectedRegion townRegion, ProtectedRegion lotRegion, String lotName) {
            this.success = true;
            this.regionManager = regionManager;
            this.townRegion = townRegion;
            this.lotRegion = lotRegion;
            this.lotName = lotName;
        }
    }

    private LotValidationResult validateLot(CommandSender sender, Town town, String lot) {
        World world = Bukkit.getWorld(worldName);
        RegionManager regionManager = WGBukkit.getPlugin().getRegionManager(world);
        String lotName = "t" + town.getId() + "_" + lot;
        ProtectedRegion townRegion = regionManager.getRegion("t" + town.getId());
        if(townRegion == null) {
            msgManager.sendMessage(sender, "error_lot_no_town_protection");
            return new LotValidationResult(false);
        }
        ProtectedRegion lotRegion = regionManager.getRegion(lotName);
        if(lotRegion == null) {
            msgManager.sendMessage(sender, "error_lot_doesnt_exist");
            return new LotValidationResult(false);
        }
        return new LotValidationResult(regionManager, townRegion, lotRegion, lotName);
    }
}
