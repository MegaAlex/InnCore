package me.megaalex.inncore.command;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("pvp")) {
            return false;
        }

        if(!(sender instanceof Player)) {
           return true;
        }

        if(!sender.hasPermission("inncore.pvp")) {
            MessageManager.sendMsg(sender, Message.NOPERM);
            return true;
        }

        InnCore.getInstance().getPvpManager().showTopKillScore((Player) sender);
        return true;
    }
}
