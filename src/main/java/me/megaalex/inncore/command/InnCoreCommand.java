package me.megaalex.inncore.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;

public class InnCoreCommand implements CommandExecutor {

    private final static String PREFIX = ChatColor.YELLOW + "[" + ChatColor.GREEN + "InnCore" + ChatColor.YELLOW + "]"
            + ChatColor.RESET + " ";

    private ArrayList<InnCoreHandler> handlers;

    public InnCoreCommand() {
        handlers = new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!cmd.getName().equals("inn")) {
            return false;
        }
        if(args.length == 0) {
            return true;
        }
        final String[] handlerArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);
        final InnCoreHandler handler = getHandler(args[0]);
        if(handler != null) {
            handler.handle(this, sender, "inn", handlerArgs);
        } else {
            sendAllHelp(sender);
        }
        return true;
    }

    private void sendAllHelp(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();
        for(final InnCoreHandler handler : getHandlers()) {
            commands.addAll(handler.getCmds(sender));
        }
        sendHelp(sender, commands);
    }

    public ArrayList<InnCoreHandler> getHandlers() {
        return handlers;
    }

    public void addHandler(final InnCoreHandler handler) {
        handlers.add(handler);
    }

    public void addAllHandlers(final List<InnCoreHandler> handlers) {
        this.handlers.addAll(handlers);
    }

    public InnCoreHandler getHandler(final String name) {
        for(final InnCoreHandler handler : handlers) {
            if(handler.getName().equals(name)) {
                return handler;
            }
        }
        return null;
    }

    public void sendHelp(final CommandSender sender, final List<String> cmds) {
        if(cmds.size() != 0 && sender != null) {
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Available commands:");
            for(final String cmd : cmds) {
                sender.sendMessage(ChatColor.GOLD + " - " + cmd);
            }
        }
    }

    public static void sendNoPerm(final CommandSender sender) {
        MessageUtils.sendMsgPrefix(sender, PREFIX, Message.NOPERM);
    }

    public static void sendError(final CommandSender sender, final String error) {
        MessageUtils.sendMsgPrefix(sender, PREFIX, Message.ERRORCUSTOM, error);
    }

    public static void sendSuccess(final CommandSender sender, final String error) {
        MessageUtils.sendMsgPrefix(sender, PREFIX, Message.SUCCESSCUSTOM, error);
    }

    public static void cmdCallback(CommandSender sender, CmdResultData result) {
        if(result != null) {
            if(result.getResult() == CmdResult.SUCCESS) {
                InnCoreCommand.sendSuccess(sender, result.getMessage());
            } else if(result.getResult() == CmdResult.FAILURE) {
                InnCoreCommand.sendError(sender, result.getMessage());
            } else if(result.getResult() == CmdResult.NOPERM) {
                InnCoreCommand.sendNoPerm(sender);
            }
        }
    }
}
