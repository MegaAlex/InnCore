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

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.utils.PluginMsgUtils;

public class GbroadcastHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "gbroadcast";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {

        if(args.length < 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        if(!sender.hasPermission("inncore.utils.gbroadcast")) {
            InnCoreCommand.sendNoPerm(sender);
            return;
        }

        if(Bukkit.getOnlinePlayers().isEmpty()) {
            InnCoreCommand.sendError(sender, "No online players!");
            return;
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("broadcast");
            out.writeUTF(sender.getName());
            out.writeUTF(getMsg(args));
            PluginMsgUtils.sendMessage(b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.utils.gbroadcast")) {
            commands.add("gbroadcast [msg] - sends message tp all servers");
        }
        return commands;
    }

    public String getMsg(String msg[]) {
        StringBuilder finalMsg = new StringBuilder();
        for(String word : msg) {
            if(finalMsg.length() > 0) {
                finalMsg.append(" ");
            }
            finalMsg.append(word);
        }
        return finalMsg.toString();
    }
}
