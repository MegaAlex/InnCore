/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.CmdResult;
import me.megaalex.inncore.command.CmdResultData;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;
import me.megaalex.inncore.news.tasks.PlayerLoginTask;

public class NewsManager extends Manager {

    private NewsSqlModule sql;
    private Integer lastBookId;

    @Override
    public String getEnableConfigName() {
        return "news.enabled";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        InnCore plugin = InnCore.getInstance();
        sql = new NewsSqlModule();
        plugin.getDatabaseManager().registerSqlModule(sql);
        plugin.getServer().getPluginManager().registerEvents(new NewsListener(), plugin);
        loadLastBookId();
    }

    private void loadLastBookId() {
        new BukkitRunnable() {

            @Override
            public void run() {
                lastBookId = sql.getLastBookId();
            }

        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void handleBookAdd(final Player player) {
        if(!player.hasPermission("inncore.news.add")) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.NOPERM, null));
            return;
        }
        ItemStack handItem = player.getItemInHand();
        if(handItem == null || handItem.getType() != Material.WRITTEN_BOOK) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "Please hold a written book to use this command!"));
            return;
        }

        BookMeta meta = (BookMeta) handItem.getItemMeta();
        long time = System.currentTimeMillis() / 1000L;
        final BookData data = new BookData(meta.getAuthor(), meta.getTitle(), meta.getPages(), time);

        new BukkitRunnable() {
            @Override
            public void run() {
                int bookId = sql.insertBook(data);

                if(bookId > 0) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.SUCCESS, "Book added successfully with Id: " + bookId + "!"));
                } else {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "Error while adding book!"));
                }
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void handleBookGet(final Player player, final String bookIdArg) {
        final InnCore plugin = InnCore.getInstance();
        if(!player.hasPermission("inncore.news.get")) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.NOPERM, null));
            return;
        }

        final int bookId;
        try {
            bookId = Integer.valueOf(bookIdArg);
        } catch (NumberFormatException e) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "The 3rd argument should be a number(book id)!"));
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!sql.bookExists(bookId)) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "Book with that Id doesn't exist!"));
                    return;
                }
                final BookData bookData = sql.getBook(bookId);
                if(bookData == null) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "An error occurred while getting book data!"));
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!player.getInventory().addItem(bookData.toItemStack()).isEmpty()) {
                            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "You don't have space in your inventory!"));
                            return;
                        }
                        InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.SUCCESS, "Book with id " + bookIdArg + " given."));
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    public void handlePlayerLogin(final Player player) {
        new PlayerLoginTask(player.getName(), sql, lastBookId).runTaskAsynchronously(InnCore.getInstance());
    }

    public void handleSetDeleted(final Player player, final boolean undelete, final String bookIdArg) {
        final InnCore plugin = InnCore.getInstance();
        if(!player.hasPermission("inncore.news.delete")) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.NOPERM, null));
            return;
        }
        final int bookId;
        try {
            bookId = Integer.valueOf(bookIdArg);
        } catch (NumberFormatException e) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "The 3rd argument should be a number(book id)!"));
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!sql.bookExists(bookId)) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "Book with that Id doesn't exist!"));
                    return;
                }
                sql.setDeleted(bookId, !undelete);
                if(undelete) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.SUCCESS, "Book with Id " + bookIdArg + " was un-deleted!"));
                    return;
                }
                InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.SUCCESS, "Book with Id " + bookIdArg + " was soft-deleted!"));
            }
        }.runTaskAsynchronously(plugin);
    }

    public void handleGetBookList(final Player player, final String bookIdArg) {
        final InnCore plugin = InnCore.getInstance();
        if(!player.hasPermission("inncore.news.list")) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.NOPERM, null));
            return;
        }
        final int page;
        try {
            page = Integer.valueOf(bookIdArg);
        } catch (NumberFormatException e) {
            InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "The 3rd argument should be a number(page)!"));
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                List<BookData> books = sql.getBookListForPage(page, false);
                if(books.isEmpty()) {
                    InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.FAILURE, "No books found!"));
                    return;
                }
                InnCoreCommand.cmdCallback(player, new CmdResultData(CmdResult.SUCCESS, Message.BOOK_LIST_HEADER.getText()));
                for(BookData book : books) {
                    Message msgType;
                    if(!book.isDeleted()) {
                        msgType = Message.BOOK_LIST_ENTRY;
                    } else {
                        msgType = Message.BOOK_LIST_ENTRY_DELETED;
                    }
                    MessageUtils.sendMsg(player, msgType, String.valueOf(book.getId()), book.getAuthor(), book.getTitle());
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
