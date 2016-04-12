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
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

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

        Player player = (Player) sender;
        ItemStack handItem = player.getItemInHand();
        if(handItem.getType() != Material.SAPLING) {
            InnCoreCommand.sendError(sender, "Hold a sapling before using this command!");
            return;
        }
        if(species == null) {
            InnCoreCommand.sendError(sender, "Unknown tree type, valid types: " + getTreeTypeList());
            return;
        }
        MaterialData data = handItem.getData();
        Tree test = new Tree(Material.SAPLING);
        if(data.getData() == species.getData()) {
            InnCoreCommand.sendError(sender, "This is the same type of sapling!");
            return;
        }
        Tree treeData = new Tree(Material.SAPLING);
        treeData.setSpecies(species);
        ///tphandItem.setData(treeData);
        ItemStack newItemStack = handItem.clone();
        newItemStack.setData(treeData);
        player.setItemInHand(newItemStack);
        //data.setData(species.getData());
        //handItem.setData(data);
        //player.setItemInHand(handItem);
        player.updateInventory();
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
