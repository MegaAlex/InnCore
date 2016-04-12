/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.npc.StaffMemberTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class StaffNpcHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "staffnpc";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(args.length < 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        final String subCmd = args[0];

        if(!(sender instanceof Player)) {
            cmd.sendError(sender, "Only players can use this command!");
            return;
        }

        final Player player = (Player) sender;
        final NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);

        if(npc == null) {
            cmd.sendError(sender, "Select a NPC before using this command!");
            return;
        }

        if(args.length <= 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        if(!npc.hasTrait(StaffMemberTrait.class)) {
            cmd.sendError(sender, "This NPC doesn't have the staff member trait!");
            return;
        }

        StaffMemberTrait trait = npc.getTrait(StaffMemberTrait.class);

        if(subCmd.equalsIgnoreCase("setName")) {
            String staffName = args[1];
            trait.setStaffName(staffName);
            sender.sendMessage(ChatColor.GREEN + "Successfully set staff member name for that hologram");
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.staffnpc.setname")) {
            commands.add("staffnpc setname [staffName] - sets staff member name");
        }
        return commands;
    }
}
