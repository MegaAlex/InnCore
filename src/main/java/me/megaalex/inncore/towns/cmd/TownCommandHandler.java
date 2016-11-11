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
import me.megaalex.inncore.utils.CmdUtils;

public class TownCommandHandler implements InnCoreHandler {

    private TownCommandResponseManager responseManger;

    public TownCommandHandler(TownCommandResponseManager responseManger) {
        this.responseManger = responseManger;
    }

    @Override
    public String getName() {
        return "town";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        if(args.length == 0) {
            sendHelp(sender, 0);
            return;
        }
        String subCmd = args[0];
        if(subCmd.equalsIgnoreCase("info")) {
            String townName = null;
            if(args.length > 1) {
               townName = args[1];
            }
            responseManger.infoCmd(sender, townName);
            return;
        }
        if(subCmd.equalsIgnoreCase("player")) {
            if(args.length != 2) {
                sendHelp(sender, 13);
                return;
            }
            responseManger.playerInfo(sender, args[1]);
            return;
        }
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
        if(subCmd.equalsIgnoreCase("deputy") || subCmd.equalsIgnoreCase("mayor")) {
            boolean mayor = subCmd.equalsIgnoreCase("mayor");
            String townName = null, player;
            switch (args.length) {
                case 1: sendHelp(sender, mayor ? 2 : 1); return;
                case 2: player = args[1]; break;
                case 3: townName = args[1]; player = args[2]; break;
                default: sendHelp(sender, mayor ? 2 : 1); return;
            }
            if(!mayor)
                responseManger.addDeputy(sender, player, townName);
            else
                responseManger.setMayor(sender, player, townName);
            return;
        }
        if(subCmd.equalsIgnoreCase("undeputy")) {
            String townName = null, player;
            switch (args.length) {
                case 1: sendHelp(sender, 12); return;
                case 2: player = args[1]; break;
                case 3: townName = args[1]; player = args[2]; break;
                default: sendHelp(sender, 12); return;
            }
            responseManger.unDeputy(sender, player, townName);
            return;
        }
        if(subCmd.equalsIgnoreCase("add") || subCmd.equalsIgnoreCase("remove") || subCmd.equalsIgnoreCase("invite")) {
            boolean remove = subCmd.equalsIgnoreCase("remove");
            String townName = null, player;
            switch (args.length) {
                case 1: sendHelp(sender, remove ? 8 : 9); return;
                case 2: player = args[1]; break;
                case 3: townName = args[1]; player = args[2]; break;
                default: sendHelp(sender, remove ? 8 : 9); return;
            }
            if(!remove)
                responseManger.addMember(sender, player, townName);
            else
                responseManger.removeMember(sender, player, townName);
            return;
        }
        if(subCmd.equalsIgnoreCase("create")) {
            if(args.length < 4) {
                sendHelp(sender, 3);
                return;
            }
            String[] merged = CmdUtils.mergeArgs(Arrays.copyOfRange(args, 1, args.length));
            if(merged.length != 3) {
                sendHelp(sender, 3);
                return;
            }
            String name = merged[0];
            String nameLong = merged[1];
            String mayorName = merged[2];
            responseManger.createTown(sender, name, nameLong, mayorName);
            return;
        }
        if(subCmd.equalsIgnoreCase("setname")) {
            if(args.length != 3) {
                sendHelp(sender, 4);
                return;
            }
            String town = args[1];
            String name = args[2];
            responseManger.setName(sender, town, name);
            return;
        }
        if(subCmd.equalsIgnoreCase("setnamelong")) {
            if(args.length < 3) {
                sendHelp(sender, 5);
                return;
            }
            String[] merged = CmdUtils.mergeArgs(Arrays.copyOfRange(args, 1, args.length));
            if(merged.length != 2) {
                sendHelp(sender, 5);
                return;
            }
            String town = merged[0];
            String name = merged[1];
            responseManger.setNameLong(sender, town, name);
            return;
        }
        if(subCmd.equalsIgnoreCase("desc")) {
            if(args.length == 1) {
                sendHelp(sender, 10);
                return;
            }
            String desc = CmdUtils.mergeArgsSimple(Arrays.copyOfRange(args, 1, args.length));
            responseManger.setDesc(sender, null, desc);
            return;
        }
        if(subCmd.equalsIgnoreCase("setdesc")) {
            if(args.length < 3) {
                sendHelp(sender, 11);
                return;
            }
            String town = args[1];
            String desc = CmdUtils.mergeArgsSimple(Arrays.copyOfRange(args, 2, args.length));
            responseManger.setDesc(sender, town, desc);
            return;
        }
        if(subCmd.equalsIgnoreCase("id")) {
            if(args.length != 2) {
                sendHelp(sender, 6);
                return;
            }
            responseManger.getId(sender, args[1]);
            return;
        }
        if(subCmd.equalsIgnoreCase("rankup")) {
            if(args.length != 2) {
                sendHelp(sender, 14);
                return;
            }
            responseManger.rankup(sender, args[1]);
            return;
        }
        if(subCmd.equalsIgnoreCase("setrank")) {
            if(args.length != 3) {
                sendHelp(sender, 15);
                return;
            }
            try {
                int rank = Integer.parseInt(args[2]);
                responseManger.setRank(sender, args[1], rank);
            } catch (NumberFormatException e) {
                sendHelp(sender, 15);
            }
            return;
        }
        if(subCmd.equalsIgnoreCase("delete")) {
            if(args.length != 2) {
                sendHelp(sender, 7);
                return;
            }
            responseManger.deleteTown(sender, args[1]);
            return;
        }
        if(subCmd.equalsIgnoreCase("deny")) {
            responseManger.memberDeny(sender);
            return;
        }
        if(subCmd.equalsIgnoreCase("accept")) {
            responseManger.memberAccept(sender);
            return;
        }
        if(subCmd.equalsIgnoreCase("leave")) {
            responseManger.leave(sender);
            return;
        }
        if(subCmd.equalsIgnoreCase("list")) {
            int page = 1;
            if(args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                }
            }
            responseManger.list(sender, page);
            return;
        }
        sendHelp(sender, 0);
    }

    public void sendHelp(CommandSender sender, int lvl) {
        sendHelp(sender, lvl, 1);
    }

    private void sendHelp(CommandSender sender, int lvl, int page) {
        ArrayList<String> msg = new ArrayList<>();
        ArrayList<String> msgAdmin = new ArrayList<>();
        MessagesManager msgManager = InnCore.getInstance().getTownsManager().getMessageManager();
        if(lvl == 0 || lvl == 1) {
            if(sender.hasPermission("inntown.deputy")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town deputy", "[igrach]",
                        "pravi jitel na grada zamestnik (samo za kmet)"}));
            }
            if(sender.hasPermission("inntown.admin.deputy")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town deputy", "[grad] [igrach]",
                        "pravi jitel na grada zamestnik (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 2) {
            if(sender.hasPermission("inntown.mayor")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town mayor", "[igrach]",
                        "pravi jitel na grada lmet (samo za kmet)"}));
            }
            if(sender.hasPermission("inntown.admin.mayor")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town mayor", "[grad] [igrach]",
                        "pravi jitel na grada kmet (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 3) {
            if(sender.hasPermission("inntown.admin.create")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town create", "[ime] [imeLong] [kmet]",
                        "osnovava grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 4) {
            if(sender.hasPermission("inntown.admin.setname")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town setname", "[grad] [ime]",
                        "promenq imeto na grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 5) {
            if(sender.hasPermission("inntown.admin.setnamelong")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town setnamelong", "[grad] [imeLong]",
                        "promenq dulgoto ime na grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 6) {
            if(sender.hasPermission("inntown.admin.id")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town id", "[grad]",
                        "pokazva id na grad"}));
            }
        }
        if(lvl == 0 || lvl == 7) {
            if(sender.hasPermission("inntown.admin.delete")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town delete", "[grad]",
                        "premahva grad"}));
            }
        }
        if(lvl == 0 || lvl == 9) {
            if(sender.hasPermission("inntown.add")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town invite/add", "[igrach]",
                        "kani igrach da se prisuedini kum grada (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.add")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town add", "[grad] [igrach]",
                        "kani igrach da se prisuedini kum grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 8) {
            if(sender.hasPermission("inntown.remove")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town remove", "[igrach]",
                        "premahva igrach ot grada (samo za kmet i zamestnici)"}));
            }
            if(sender.hasPermission("inntown.admin.remove")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town remove", "[grad] [igrach]",
                        "premahva igrach ot grad (admin cmd)"}));
            }
        }
        if(lvl == 0 || lvl == 10) {
            if(sender.hasPermission("inntown.desc")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town desc", "[opisanie]",
                        "promenq opisanieto nma grada (samo za kmet)"}));
            }
        }
        if(lvl == 0 || lvl == 11) {
            if(sender.hasPermission("inntown.admin.setdesc")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town setdesc", "[grad] [opisanie]",
                        "promenq opisanieto nma grad (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 12) {
            if(sender.hasPermission("inntown.undeputy")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town undeputy", "[igrach]",
                        "ponijava igrach do jitel (samo za kmet)"}));
            }
            if(sender.hasPermission("inntown.admin.undeputy")) {
                msgAdmin.add(msgManager.formatMessage("help_entry", new String[]{"/town undeputy", "[grad] [igrach]",
                        "ponijava igrach v grad do jitel (admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 13) {
            if(sender.hasPermission("inntown.player")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town player", "[igrach]",
                        "pokazva tekushtiq grad na igrach"}));
            }
        }

        if(lvl == 0 || lvl == 14) {
            if(sender.hasPermission("inntown.admin.rankup")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town player", "[grad]",
                        "rankup-va grad(admin cmd)"}));
            }
        }

        if(lvl == 0 || lvl == 15) {
            if(sender.hasPermission("inntown.admin.setrank")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town setrank", "[grad] [rank]",
                        "promenq ranka na grad(admin cmd)"}));
            }
        }

        if(lvl == 0) {
            if(sender.hasPermission("inntown.info.self")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town info", "",
                        "pokazva informaciq za tekushtiq ti grad"}));
            }

            if(sender.hasPermission("inntown.info.others")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town info", "[town]",
                        "pokazva informaciq za drug grad"}));
            }

            if(sender.hasPermission("inntown.accept")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town accept", "",
                        "priemash pokana da se prisuedinish kum grad"}));
            }
            if(sender.hasPermission("inntown.deny")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town deny", "",
                        "otkazvash pokana da se prisuedinish kum grad"}));
            }
            if(sender.hasPermission("inntown.leave")) {
                msg.add(msgManager.formatMessage("help_entry", new String[]{"/town leave", "",
                        "napuskash grad"}));
            }
        }
        msg.addAll(msgAdmin);
        if(lvl == 0) {
            page --;
            int pages = msg.size() % 4 == 0 ? msg.size() / 4 : msg.size() / 4 + 1;
            msgManager.sendMessage(sender, "help_head", String.valueOf(page + 1), String.valueOf(pages));
            if(msg.size() < page * 4) {
                return;
            }
            for(int i = page * 4; i <= page * 4 + 3 && i < msg.size(); i++) {
                sender.sendMessage(msg.get(i));
            }
            msgManager.sendMessage(sender, "townlot_footer_page", String.valueOf(page + 1), String.valueOf(pages), "town help");
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
