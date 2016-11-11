/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.towns.MessagesManager;

public class LotsCommandHandler implements InnCoreHandler {

    LotsCommandResponseManager responseManager;

    public LotsCommandHandler(LotsCommandResponseManager responseManager) {
        this.responseManager = responseManager;
    }

    @Override
    public String getName() {
        return "lot";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(args.length == 0) {
            sendHelp(sender, 0);
            return;
        }
        String subCmd = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        if(subCmd.equalsIgnoreCase("help")) {
            int page = 1;
            try {
                if(args.length > 1)
                    page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
            sendHelp(sender, 0, page);
            return;
        }
        if(subCmd.equalsIgnoreCase("define") || subCmd.equalsIgnoreCase("create")) {
            ArgsData data = getArgsData(sender, newArgs, 1);
            if(data.success)
                responseManager.define(sender, data.townName, data.lotName, data.flags);
            return;
        }
        if(subCmd.equalsIgnoreCase("remove") || subCmd.equalsIgnoreCase("delete")) {
            ArgsData data = getArgsData(sender, newArgs, 2);
            if(data.success)
                responseManager.remove(sender, data.townName, data.lotName);
            return;
        }
        if(subCmd.equalsIgnoreCase("redefine") || subCmd.equalsIgnoreCase("recreate")) {
            ArgsData data = getArgsData(sender, newArgs, 3);
            if(data.success)
                responseManager.redefine(sender, data.townName, data.lotName, data.flags);
            return;
        }
        if(subCmd.equalsIgnoreCase("clearplayers")) {
            ArgsData data = getArgsData(sender, newArgs, 4);
            if(data.success)
                responseManager.clearPlayers(sender, data.townName, data.lotName);
            return;
        }
        if(subCmd.equalsIgnoreCase("info")) {
            ArgsData data = getArgsData(sender, newArgs, 5);
            if(data.success)
                responseManager.info(sender, data.townName, data.lotName);
            return;
        }

        if(subCmd.equalsIgnoreCase("addowner") || subCmd.equalsIgnoreCase("removeowner")) {
            boolean remove = subCmd.equalsIgnoreCase("removeowner");
            String townName = null, playerName, lotName;
            switch (newArgs.length) {
                case 2: lotName = newArgs[0]; playerName = newArgs[1]; break;
                case 3: townName = newArgs[0]; lotName = newArgs[1]; playerName = newArgs[2]; break;
                default: sendHelp(sender, remove ? 7 : 6); return;
            }
            if(!remove) {
                responseManager.addOwner(sender, townName, lotName, playerName);
            } else {
                responseManager.removeOwner(sender, townName, lotName, playerName);
            }
        }
    }

    private ArgsData getArgsData(CommandSender sender, String[] args, int helpId) {
        String flags = null;
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        for(String arg : argList) {
            if(arg.startsWith("-")) {
                flags = arg.substring(1);
                break;
            }
        }
        argList.remove("-" + flags);
        switch (argList.size()) {
            case 0: sendHelp(sender, helpId); return new ArgsData(false);
            case 1: return new ArgsData(args[0], flags);
            case 2: return new ArgsData(args[0], args[1], flags);
            default: sendHelp(sender, helpId); return new ArgsData(false);
        }
    }

    private class ArgsData {
        boolean success;
        String townName;
        String lotName;
        String flags = null;

        ArgsData(String townName, String lotName, String flags) {
            this.success = true;
            this.townName = townName;
            this.lotName = lotName;
            this.flags = flags;
        }

        ArgsData(boolean success) {
            this.success = success;
        }

        ArgsData(String lotName, String flags) {
            this.success = true;
            this.townName = null;
            this.lotName = lotName;
            this.flags = flags;
        }
    }

    public void sendHelp(CommandSender sender, int lvl) {
        sendHelp(sender, lvl, 1);
    }

    private void sendHelp(CommandSender sender, int lvl, int page) {
        ArrayList<String> msg = new ArrayList<>();
        ArrayList<String> msgAdmin = new ArrayList<>();
        MessagesManager msgManager = InnCore.getInstance().getTownsManager().getMessageManager();
        if(lvl == 0 || lvl == 1) {
            if(sender.hasPermission("inntown.lot.define")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot define/create", "[ime parcel] (-n)",
                        "suzdava parcel (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.lot.define")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot define/create", "[grad] [ime parcel] (-n)",
                        "suzdava parcel v grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 2) {
            if(sender.hasPermission("inntown.lot.remove")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot remove", "[ime parcel]",
                        "premahva parcel (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.lot.remove")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot remove", "[grad] [ime parcel]",
                        "premahva parcel v grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 3) {
            if(sender.hasPermission("inntown.lot.redefine")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot redefine/recreate", "[ime parcel] (-n)",
                        "promenq teritoriqta na parcel (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.lot.redefine")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot redefine/recreate", "[grad] [ime parcel] (-n)",
                        "promenq teritoriqta na parcel v grad (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 4) {
            if(sender.hasPermission("inntown.lot.clearplayers")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot clearplayers", "[ime parcel]",
                        "premahva vsichki igrachi ot  parcel (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.lot.clearplayers")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot clearplayers", "[grad] [ime parcel]",
                        "premahva vsichki igrachi ot parcel v grad (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 5) {
            if(sender.hasPermission("inntown.lot.info")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot info", "[ime parcel]",
                        "pokazva informaciq za parcel"}));
            }
            if(sender.hasPermission("inntown.lot.info.others")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot info", "[grad] [ime parcel]",
                        "pokazva informaciq za parcel v grad (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 6) {
            if(sender.hasPermission("inntown.lot.addowner")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot addowner", "[ime parcel] [igrach]",
                        "dobavq sobstvenik kum parcel"}));
            }
            if(sender.hasPermission("inntown.admin.lot.addowner")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot addowner", "[grad] [ime parcel] [igrach]",
                        "dobavq sobstvenik kum parcel v grad (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 7) {
            if(sender.hasPermission("inntown.lot.removeowner")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/lot removeowner", "[ime parcel] [igrach]",
                        "premahva sobstvenik ot parcel"}));
            }
            if(sender.hasPermission("inntown.admin.lot.removeowner")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/lot removeowner", "[grad] [ime parcel] [ograch]",
                        "premahva sobstvenik ot parcel v grad (admin cmd)"}));
            }
        }
        msg.addAll(msgAdmin);

        if(lvl == 0) {
            page --;
            int pages = msg.size() % 4 == 0 ? msg.size() / 4 : msg.size() / 7 + 1;
            msgManager.sendMessage(sender, "help_head", String.valueOf(page + 1), String.valueOf(pages));
            if(msg.size() < page * 4) {
                return;
            }
            for(int i = page * 4; i <= page * 4 + 3 && i < msg.size(); i++) {
                sender.sendMessage(msg.get(i));
            }
            msgManager.sendMessage(sender, "townlot_footer_page", String.valueOf(page + 1), String.valueOf(pages), "lot help");
        } else {
            if(msg.isEmpty()) {
                sendHelp(sender, 0);
                return;
            }
            for(String message : msg) {
                sender.sendMessage(message);
            }
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        return Collections.emptyList();
    }
}
