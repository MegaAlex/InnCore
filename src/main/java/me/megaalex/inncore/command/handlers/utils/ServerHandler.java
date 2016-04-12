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

public class ServerHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        String serverName = usedCmd;
        if(!(sender instanceof Player) || args.length > 1
                || (args.length == 1 && !usedCmd.equalsIgnoreCase("inn"))
                || (args.length == 0 && usedCmd.equalsIgnoreCase("inn")) ) {
            return;
        }
        Player player = (Player) sender;

        if(args.length == 1) {
            serverName = args[0];
        }

        InnCore.getInstance().getMiscManager().handleServerCommand(player, serverName);

    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        return new ArrayList<>();
    }
}
