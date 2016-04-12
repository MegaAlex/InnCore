/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;

public class SetSpawnHandler implements InnCoreHandler {
    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(!sender.hasPermission("inncore.utils.setspawn")) {
            InnCoreCommand.sendNoPerm(sender);
            return;
        }
        if(!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        InnCore.getInstance().getMiscManager().setSpawnLocation(player.getLocation());
        InnCoreCommand.sendSuccess(sender, "Successfully set spawn location!");
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.utils.setspawn")) {
            commands.add("setspawm - sets the server spawn");
        }
        return commands;
    }
}
