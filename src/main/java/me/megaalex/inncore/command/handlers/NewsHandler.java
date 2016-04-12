/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.command.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.news.NewsManager;

public class NewsHandler implements InnCoreHandler {

    @Override
    public String getName() {
        return "news";
    }

    @Override
    public void handle(InnCoreCommand cmd, CommandSender sender, String usedCmd, String[] args) {
        NewsManager news = InnCore.getInstance().getNewsManager();
        if(args.length < 1) {
            cmd.sendHelp(sender, getCmds(sender));
            return;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("News is a player only command!");
            return;
        }

        Player player = (Player) sender;
        if(args[0].equalsIgnoreCase("add")) {
            news.handleBookAdd(player);
            return;
        }
        if(args[0].equalsIgnoreCase("get")) {
            if(args.length != 2) {
                cmd.sendHelp(sender, getCmds(sender));
                return;
            }
            news.handleBookGet(player, args[1]);
            return;
        }
        if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("undelete")) {
            if(args.length != 2) {
                cmd.sendHelp(sender, getCmds(sender));
                return;
            }
            boolean undelete = args[0].equalsIgnoreCase("undelete");
            news.handleSetDeleted(player, undelete, args[1]);
            return;
        }
        if(args[0].equalsIgnoreCase("list")) {
            String pageArg = "1";
            if(args.length > 1) {
                pageArg = args[1];
            }
            news.handleGetBookList(player, pageArg);
        }
    }

    @Override
    public List<String> getCmds(CommandSender sender) {
        final ArrayList<String> commands = new ArrayList<>();

        if(sender.hasPermission("inncore.news.add")) {
            commands.add("news add - add a book");
        }
        if(sender.hasPermission("inncore.news.get")) {
            commands.add("news get [bookId] - add a book");
        }
        if(sender.hasPermission("inncore.news.list")) {
            commands.add("news list (page) - get a list of books");
        }
        if(sender.hasPermission("inncore.news.delete")) {
            commands.add("news delete [bookId] - delete a book");
        }
        if(sender.hasPermission("inncore.news.undelete")) {
            commands.add("news undelete [bookId] - undelete a book");
        }

        return commands;
    }
}
