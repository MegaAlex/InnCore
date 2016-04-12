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
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(args.length < 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        final String subCmd = args[0];

        if(!(sender instanceof Player)) {
            InnCoreCommand.sendError(sender, "Only players can use this command!");
            return;
        }

        final Player player = (Player) sender;
        final NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);

        if(npc == null) {
            InnCoreCommand.sendError(sender, "Select a NPC before using this command!");
            return;
        }

        if(args.length <= 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        if(!npc.hasTrait(RaceSelectTrait.class)) {
            InnCoreCommand.sendError(sender, "This NPC doesn't have the race select trait!");
            return;
        }

        final RaceSelectTrait trait = npc.getTrait(RaceSelectTrait.class);

        if(subCmd.equalsIgnoreCase("setgroupname")) {
            final String groupName = args[1];

            if(!sender.hasPermission("inncore.npc.setgroupname")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.groupName = groupName;
            InnCoreCommand.sendSuccess(sender, "Set group Name successfully!");
            return;
        }

        if(subCmd.equalsIgnoreCase("setracename")) {
            final String raceName = args[1];

            if(!sender.hasPermission("inncore.npc.setracename")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.raceName = raceName;
            InnCoreCommand.sendSuccess(sender, "Set race name successfully!");
            return;
        }

        if(subCmd.equalsIgnoreCase("clickagain")) {
            String clickMsg = getMsg(args);

            if(!sender.hasPermission("inncore.npc.clickagain")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.clickAgain = clickMsg;
            InnCoreCommand.sendSuccess(sender, "Set click again message successfully!");
        }

        if(subCmd.equalsIgnoreCase("selecttext")) {
            String selectMsg = getMsg(args);

            if(!sender.hasPermission("inncore.npc.selecttext")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.selectText = selectMsg;
            InnCoreCommand.sendSuccess(sender, "Set select race message successfully!");
        }

        if(subCmd.equalsIgnoreCase("alreadyjoined")) {
            String alreadyJoinedMessage = getMsg(args);

            if(!sender.hasPermission("inncore.npc.alreadyjoined")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.alreadyJoined = alreadyJoinedMessage;
            InnCoreCommand.sendSuccess(sender, "Set already joined message successfully!");
        }

        if(subCmd.equalsIgnoreCase("announcetext")) {
            String announceText = getMsg(args);
            if(announceText.isEmpty()) {
                announceText = null;
            }

            if(!sender.hasPermission("inncore.npc.announcetext")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.joinAnnounceText = announceText;
            InnCoreCommand.sendSuccess(sender, "Set join broadcast message successfully!");
        }

        if(subCmd.equalsIgnoreCase("leftclicktext")) {
            String leftClickText = getMsg(args);
            if(leftClickText.isEmpty()) {
                leftClickText = null;
            }

            if(!sender.hasPermission("inncore.npc.leftclicktext")) {
                InnCoreCommand.sendNoPerm(sender);
                return;
            }

            trait.leftClickText = leftClickText;
            InnCoreCommand.sendSuccess(sender, "Set left click message successfully!");
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
        if(sender.hasPermission("inncore.npc.alreadyjoined")) {
            commands.add("npc alreadyjoined [text] - sets the already joined msg");
        }
        if(sender.hasPermission("inncore.npc.announcetext")) {
            commands.add("npc announcetext [text] - sets the announce msg");
        }
        if(sender.hasPermission("inncore.npc.leftclicktext")) {
            commands.add("npc leftclicktext [text] - sets the left click msg");
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
