package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.npc.RaceSelectTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NpcHandler implements InnCoreHandler {
    @Override
    public String getName() {
        return "npc";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String[] args) {
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

        if(!npc.hasTrait(RaceSelectTrait.class)) {
            cmd.sendError(sender, "This NPC doesn't have the race select trait!");
            return;
        }

        final RaceSelectTrait trait = npc.getTrait(RaceSelectTrait.class);

        if(subCmd.equals("setgroupname")) {
            final String groupName = args[1];

            if(!sender.hasPermission("inncore.npc.setgroupname")) {
                cmd.sendNoPerm(sender);
                return;
            }

            trait.groupName = groupName;
            cmd.sendSuccess(sender, "Set group Name successfully!");
            return;
        }

        if(subCmd.equals("setracename")) {
            final String raceName = args[1];

            if(!sender.hasPermission("inncore.npc.setracename")) {
                cmd.sendNoPerm(sender);
                return;
            }

            trait.raceName = raceName;
            cmd.sendSuccess(sender, "Set race name successfully!");
            return;
        }

        if(subCmd.equals("clickagain")) {
            String clickMsg = getMsg(args);

            if(!sender.hasPermission("inncore.npc.clickagain")) {
                cmd.sendNoPerm(sender);
                return;
            }

            trait.clickAgain = clickMsg;
            cmd.sendSuccess(sender, "Set click again message successfully!");
        }

        if(subCmd.equals("selecttext")) {
            String selectMsg = getMsg(args);

            if(!sender.hasPermission("inncore.npc.selecttext")) {
                cmd.sendNoPerm(sender);
                return;
            }

            trait.selectText = selectMsg;
            cmd.sendSuccess(sender, "Set select race message successfully!");
        }
    }

    @Override
    public List<String> getCmds(final CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.npc.setgroupname")) {
            commands.add("npc setgroupname [groupName] - sets perm group name");
        }
        if(sender.hasPermission("inncore.npc.setracename")) {
            commands.add("npc setracename [raceName] - sets race name.");
        }
        if(sender.hasPermission("inncore.npc.clickagain")) {
            commands.add("npc clickagain [message] - sets click again msg");
        }
        if(sender.hasPermission("inncore.npc.selecttext")) {
            commands.add("npc selecttext [message] - sets race select msg");
        }
        if(sender.hasPermission("inncore.npc.undertext")) {
            commands.add("npc undertext [text] - sets under text");
        }
        return commands;
    }

    private String getMsg(final String[] args) {
        String msg = "";
        for(int i = 1; i < args.length; i++) {
            if(i != 1) {
                msg += " ";
            }

            msg += args[i];
        }

        return msg;
    }
}
