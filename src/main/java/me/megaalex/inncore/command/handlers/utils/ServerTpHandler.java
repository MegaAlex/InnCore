/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.utils.PluginMsgUtils;

public class ServerTpHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "stp";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {

        if(args.length < 1 || args.length > 2) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        String perm = args.length == 1 ? "inncore.utils.stp.self"
                : "inncore.utils.stp.others";

        if(!sender.hasPermission(perm)) {
            InnCoreCommand.sendNoPerm(sender);
            return;
        }

        String server, player;
        if(args.length == 1) {
            player = sender.getName();
            server = args[0];
        } else {
            player = args[0];
            server = args[1];
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("stp");
            out.writeUTF(sender.getName());
            out.writeUTF(player);
            out.writeUTF(server);
            PluginMsgUtils.sendMessage(b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.utils.stp.self")) {
            commands.add("stp [server] - sends you to other server");
        }

        if(sender.hasPermission("inncore.utils.stp.others")) {
            commands.add("stp [player] [server] - sends a player to other server");
        }
        return commands;
    }
}
