package me.megaalex.inncore.command;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.credits.ChangeTask;
import me.megaalex.inncore.credits.CheckTask;
import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("credits")) {
            return false;
        }

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
                return true;
            }

            processPayment(sender, args, changeType,
                    null, "Console");

        }

        if(!(sender instanceof Player)) {
            return true;
        }

        final String senderName = sender.getName();
        final UUID senderId = ((Player) sender).getUniqueId();

        if(args.length == 0) {
            // showHelp(sender, 1);
            if(!sender.hasPermission("inncraft.credits")) {
                MessageManager.sendMsg(sender, Message.NOPERM);
                return true;
            }
            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new CheckTask(senderId, senderName, senderName));
            return true;
        }

        String subCmd = args[0];

        if(subCmd.equalsIgnoreCase("help")) {
            showHelp(sender, 1);
            return true;
        }

        if(subCmd.equalsIgnoreCase("show")) {
            if(!sender.hasPermission("inncraft.credits.show")) {
                MessageManager.sendMsg(sender, Message.NOPERM);
                return true;
            }
            if(args.length != 2) {
                showHelp(sender, 20);
                return true;
            }

            final String checkPlayer = args[1];
            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new CheckTask(senderId, senderName, checkPlayer));
            return true;
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
                MessageManager.sendMsg(sender, Message.NOPERM);
                return true;
            }
            if(args.length != 3) {
                showHelp(sender, helpType);
                return true;
            }
            processPayment(sender, args, changeType,
                    senderId, senderName);
            return true;
        }

        showHelp(sender, 1);

        return true;
    }

    private void processPayment(CommandSender sender, String args[],
                                ChangeTask.ChangeType changeType, UUID senderId,
                                String senderName) {

        try {
            final String receiverName = args[1];
            final BigDecimal amount = new BigDecimal(String.format("%.2f",
                    Double.parseDouble(args[2])));

            InnCore.getInstance().getServer().getScheduler().runTaskAsynchronously(
                    InnCore.getInstance(), new ChangeTask(changeType, senderId,
                            senderName, receiverName, amount));
        } catch (NumberFormatException e) {
            showHelp(sender, 10);
        }
    }

    private void showHelp(CommandSender sender, int type) {
        if(!sender.hasPermission("inncraft.credits.help")) {
            MessageManager.sendMsg(sender, Message.NOPERM);
            return;
        }
        MessageManager.sendMsg(sender, Message.HELP_HEADER);
        if(type == 1) {
            if(sender.hasPermission("inncraft.credits"))
                MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                        "/credits", "Pokazva kolko kredita imash");
            if(sender.hasPermission("inncraft.credits.help"))
                MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                        "/credits help", "Pokazva pomoshtna informaciq");
        }

        if((type == 1 || type == 10)
                && sender.hasPermission("inncraft.credits.send")) {
            MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                    "/credits send [igrach] [bori]", "Izprashta krediti na igrach");
        }

        if((type == 1 || type == 20)
                && sender.hasPermission("inncraft.credits.show")) {
            MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                    "/credits show [igrach]", "Pokazva kolko kredita ima igrach");
        }

        if((type == 1 || type == 30)
                && sender.hasPermission("inncraft.credits.grant")) {
            MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                    "/credits grant [igrach] [bori]", "Dava krediti na igrach");
        }

        if((type == 1 || type == 40)
                && sender.hasPermission("inncraft.credits.deduct")) {
            MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                    "/credits deduct [igrach] [bori]", "Vzima krediti ot igrach");
        }

        if((type == 1 || type == 50)
                && sender.hasPermission("inncraft.credits.set")) {
            MessageManager.sendMsg(sender, Message.HELP_COMMNAD,
                    "/credits set [igrach] [bori]", "Promenq broq krediti na igrach");
        }
    }
}
