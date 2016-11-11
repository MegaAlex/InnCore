/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.sky.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sapling;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;

public class SaplingHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "sapling";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(!(sender instanceof Player)) {
            return;
        }

        if(!sender.hasPermission("inncore.sky.sapling")) {
            InnCoreCommand.sendNoPerm(sender);
            return;
        }

        if(args.length != 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        String type = args[0];
        TreeSpecies species = getSpecies(type);

        final Player player = (Player) sender;
        final ItemStack handItem = player.getInventory().getItemInMainHand().clone();
        if(handItem.getType() != Material.SAPLING) {
            InnCoreCommand.sendError(sender, "Hold a sapling before using this command!");
            return;
        }
        if(species == null) {
            InnCoreCommand.sendError(sender, "Unknown tree type, valid types: " + getTreeTypeList());
            return;
        }
        Sapling sapling = (Sapling) handItem.getData();
        if(sapling.getSpecies() == species) {
            InnCoreCommand.sendError(sender, "This is the same type of sapling!");
            return;
        }
        sapling.setSpecies(species);
        handItem.setDurability(sapling.getData());
        new BukkitRunnable() {

            @Override
            public void run() {
                player.getInventory().setItemInMainHand(handItem);
            }
        }.runTask(InnCore.getInstance());

        //player.updateInventory();
        InnCoreCommand.sendSuccess(sender, "Successfully changed sapling type!");

    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.sky.sapling")) {
            commands.add("sapling [type] - change sapling type");
        }
        return commands;
    }

    String getTreeTypeList() {
        String typeString = "";
        for(TreeSpecies type : TreeSpecies.values()) {
            if(!typeString.isEmpty()) {
                typeString += ", ";
            }
            typeString += type.toString().toLowerCase();
        }
        return typeString;
    }

    TreeSpecies getSpecies(String name) {
        for(TreeSpecies species : TreeSpecies.values()) {
            if(species.toString().equalsIgnoreCase(name)) {
                return species;
            }
        }
        return null;
    }
}
