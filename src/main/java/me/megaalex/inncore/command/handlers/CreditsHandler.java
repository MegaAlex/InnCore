package me.megaalex.inncore.command.handlers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.credits.ChangeTask;
import me.megaalex.inncore.credits.CheckTask;

public class CreditsHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String[] args) {

        if((sender instanceof ConsoleCommandSender) && args.length == 3
                && (args[0].equalsIgnoreCase("grant")|| args[0].equalsIgnoreCase("deduct")
                || args[0].equalsIgnoreCase("set"))) {
            String subCmd = args[0];
            ChangeTask.ChangeType changeType;
            if(subCmd.equalsIgnoreCase("set")) {
                changeType = ChangeTask.ChangeType.SET;
            } else if(subCmd.equalsIgnoreCase("grant")) {
                changeType = ChangeTask.ChangeType.GRANT;
            } else if(subCmd.equalsIgnoreCase("deduct")) {
                changeType = ChangeTask.ChangeType.DEDUCT;
            } else {
                return;
            }

            processPayment(sender, args, changeType,
                    null, "Console", cmd);

        }

        if(!(sender instanceof Player)) {
            return;
        }

        final String senderName = sender.getName();
        final UUID senderId = ((Player) sender).getUniqueId();

        if(args.length == 0) {
            // showHelp(sender, 1);
            if(!sender.hasPermission("inncraft.credits")) {
                cmd.sendNoPerm(sender);
                return;
            }
            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new CheckTask(senderId, senderName, senderName));
            return;
        }

        String subCmd = args[0];

        if(subCmd.equalsIgnoreCase("help")) {
            sendHelp(sender, 1, cmd);
            return;
        }

        if(subCmd.equalsIgnoreCase("show")) {
            if(!sender.hasPermission("inncraft.credits.show")) {
                cmd.sendNoPerm(sender);
                return;
            }
            if(args.length != 2) {
                cmd.sendHelp(sender, getCmds(sender));
                return;
            }

            final String checkPlayer = args[1];
            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new CheckTask(senderId, senderName, checkPlayer));
            return;
        }

        if(subCmd.equalsIgnoreCase("send") || subCmd.equalsIgnoreCase("grant")
                || subCmd.equalsIgnoreCase("deduct") || subCmd.equalsIgnoreCase("set")) {

            String perm;
            ChangeTask.ChangeType changeType = ChangeTask.ChangeType.SEND;
            int helpType;
            if(subCmd.equalsIgnoreCase("send")) {
                perm = "inncraft.credits.send";
                helpType = 10;
            } else if(subCmd.equalsIgnoreCase("grant")) {
                changeType = ChangeTask.ChangeType.GRANT;
                perm = "inncraft.credits.grant";
                helpType = 30;
            } else if(subCmd.equalsIgnoreCase("deduct")) {
                changeType = ChangeTask.ChangeType.DEDUCT;
                perm = "inncraft.credits.deduct";
                helpType = 40;
            } else {
                changeType = ChangeTask.ChangeType.SET;
                perm = "inncraft.credits.set";
                helpType = 50;
            }

            if(!sender.hasPermission(perm)) {
                cmd.sendNoPerm(sender);
                return;
            }
            if(args.length != 3) {
                sendHelp(sender, helpType, cmd);
                return;
            }
            processPayment(sender, args, changeType,
                    senderId, senderName, cmd);
            return;
        }

        sendHelp(sender, 1, cmd);
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> cmds = new ArrayList<>();
        if(sender.hasPermission("inncraft.credits"))
            cmds.add("credits - Pokazva kolko kredita imash");
        if(sender.hasPermission("inncraft.credits.help"))
            cmds.add("credits help - pokazva pomoshtna informaciq");
        if(sender.hasPermission("inncraft.credits.send")) {
            cmds.add("credits send [igrach] [bori] - Izprashta krediti na igrach");
        }
        if(sender.hasPermission("inncraft.credits.show")) {
            cmds.add("credits show [igrach] - Pokazva kolko kredita ima igrach");
        }

        if(sender.hasPermission("inncraft.credits.grant")) {
            cmds.add("credits grant [igrach] [bori] - Dava krediti na igrach");
        }

        if(sender.hasPermission("inncraft.credits.deduct")) {
            cmds.add("credits deduct [igrach] [bori] - Vzima krediti ot igrach");
        }

        if(sender.hasPermission("inncraft.credits.set")) {
            cmds.add("credits set [igrach] [bori] - Promenq broq krediti na igrach");
        }
        return cmds;
    }

    private void processPayment(CommandSender sender, String args[],
                                ChangeTask.ChangeType changeType, UUID senderId,
                                String senderName, InnCoreCommand cmd) {

        try {
            final String receiverName = args[1];
            final BigDecimal amount = new BigDecimal(String.format("%.2f",
                    Double.parseDouble(args[2])));

            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new ChangeTask(changeType, senderId,
                            senderName, receiverName, amount));
        } catch (NumberFormatException e) {
            sendHelp(sender, 10, cmd);
        }
    }

    private void sendHelp(CommandSender sender, int type, InnCoreCommand cmd) {
        final ArrayList<String> cmds = new ArrayList<>();
        if(!sender.hasPermission("inncraft.credits.help")) {
            return;
        }

        if(type == 1) {
            if(sender.hasPermission("inncraft.credits"))
                cmds.add("/credits - Pokazva kolko kredita imash");
            if(sender.hasPermission("inncraft.credits.help"))
                cmds.add("/credits help - Pokazva pomoshtna informaciq");
        }
        if((type == 1 || type == 10)
                && sender.hasPermission("inncraft.credits.send")) {
            cmds.add("/credits send [igrach] [bori] - Izprashta krediti na igrach");
        }

        if((type == 1 || type == 20)
                && sender.hasPermission("inncraft.credits.show")) {
            cmds.add("/credits show [igrach] - Pokazva kolko kredita ima igrach");
        }

        if((type == 1 || type == 30)
                && sender.hasPermission("inncraft.credits.grant")) {
            cmds.add("/credits grant [igrach] [bori] - Dava krediti na igrach");
        }

        if((type == 1 || type == 40)
                && sender.hasPermission("inncraft.credits.deduct")) {
            cmds.add("/credits deduct [igrach] [bori] - Vzima krediti ot igrach");
        }

        if((type == 1 || type == 50)
                && sender.hasPermission("inncraft.credits.set")) {
            cmds.add("/credits set [igrach] [bori] - Promenq broq krediti na igrach");
        }
        cmd.sendHelp(sender, cmds);
    }
}
