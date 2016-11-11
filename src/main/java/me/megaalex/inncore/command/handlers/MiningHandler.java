/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.factionsmisc.MiningLevelManagerListener;
import net.md_5.bungee.api.ChatColor;

public class MiningHandler implements InnCoreHandler {
    @Override
    public String getName() {
        return "mining";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        MiningLevelManagerListener miningManager = InnCore.getInstance().getFactionsMiscManager().getMiningManager();
        if(miningManager.testingAssert(player)) {
            return;
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("setspawn")) {
                if (!sender.hasPermission("inncore.mining.setspawn")) {
                    return;
                }
                miningManager.setSpawnPoint(player);
                sender.sendMessage(ChatColor.GREEN + "Mining spawn point set at your location!");
                return;
            }
            if(args[0].equalsIgnoreCase("maxlevel")) {
                if (!sender.hasPermission("inncore.mining.maxlevel")) {
                    return;
                }
                int highestLvl = miningManager.getHighestLvlForPlayer(player);
                player.sendMessage(ChatColor.GREEN + "Max mining level: " + highestLvl);
            }
        }
        int lvl = miningManager.getHighestLvlForPlayer(player);
        if(args.length != 0) {
            try {
                int argsLvl = Integer.parseInt(args[0]);
                if (argsLvl > lvl && lvl != -1) {
                    sender.sendMessage(ChatColor.RED + "You are not allowed to be in that mining level!");
                    return;
                }
                if (argsLvl < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mining [level]");
                    return;
                }
                lvl = argsLvl;
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Usage: /mining [level]");
                return;
            }
        }

        miningManager.teleportPlayerToZone(player, lvl);

    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("mining [level] - teleports you to the mining world");
        if(sender.hasPermission("inncore.mining.setspawn")) {
            commands.add("mining setspawn - sets the main point of the world");
        }
        return commands;
    }
}
