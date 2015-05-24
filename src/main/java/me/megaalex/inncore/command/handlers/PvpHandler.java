package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;

public class PvpHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "pvp";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
           return;
        }
        if(!sender.hasPermission("inncore.pvp")) {
            cmd.sendNoPerm(sender);
            return;
        }
        InnCore.getInstance().getPvpManager().showTopKillScore((Player) sender);
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        ArrayList<String> cmds = new ArrayList<>();
        if (sender.hasPermission("inncore.pvp")) {
            cmds.add("pvp - shows pvp scoreboard");
        }
        return cmds;
    }
}
