/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;

public class PaperHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "paper";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(args.length < 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }
        String subCmd = args[0];
        if(subCmd.equalsIgnoreCase("setview")) {
            if(!sender.hasPermission("inncore.paper.setview")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }
            if(args.length != 2) {
                cmd.sendHelp(sender, getCmds(sender));
                return;
            }
            try {
                int distance = Integer.parseInt(args[1]);
                if(distance < 1 || distance > 8) {
                    InnCoreCommand.sendError(sender, "Use Distance (1,8]");
                    return;
                }
                InnCore.getInstance().getPaperManager().setViewDistance(distance);
                InnCoreCommand.sendSuccess(sender, "Distance successfully updated.");
            } catch (NumberFormatException ignored) {
                cmd.sendHelp(sender, getCmds(sender));
                return;
            }
        } else if(subCmd.equalsIgnoreCase("showView")) {
            if (!sender.hasPermission("inncore.paper.showview")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }
            int distance = InnCore.getInstance().getPaperManager().getViewDistance();
            InnCoreCommand.sendSuccess(sender, "Current view distance: " + distance);
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        ArrayList<String> cmds = new ArrayList<>();
        if (sender.hasPermission("inncore.paper.setview")) {
            cmds.add("setview [distance] - sets the view distance");
        }
        if (sender.hasPermission("inncore.paper.showview")) {
            cmds.add("showView - shows the current view distance");
        }
        return cmds;
    }
}
