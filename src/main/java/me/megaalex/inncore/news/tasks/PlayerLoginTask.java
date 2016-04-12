/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.news.BookData;
import me.megaalex.inncore.news.LastViewedData;
import me.megaalex.inncore.news.NewsSqlModule;

public class PlayerLoginTask extends BukkitRunnable {

    private final String playerName;
    private final NewsSqlModule sql;
    private final int lastBookId;

    public PlayerLoginTask(String playerName, NewsSqlModule sql, int lastBookId) {
        this.playerName = playerName;
        this.sql = sql;
        this.lastBookId = lastBookId;
    }


    @Override
    public void run() {
        final InnCore plugin = InnCore.getInstance();
        LastViewedData lastViewedBook = sql.getLastViewed(playerName);
        if(lastViewedBook == null || lastViewedBook.getBookId() < lastBookId) {
            final int lastViewedBookId = lastViewedBook == null ? 0 : lastViewedBook.getBookId();
            final List<BookData> booksToAdd = sql.getNotDeletedBooksAfter(lastViewedBookId);
            if(booksToAdd.isEmpty()) {
                sql.setLastViewed(playerName, lastBookId);
                return;
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    Player player = Bukkit.getPlayerExact(playerName);
                    if(player == null) {
                        return;
                    }
                    Inventory inv = player.getInventory();
                    for(int i = 0; i < booksToAdd.size() && i < 7; i++) {
                        BookData book = booksToAdd.get(i);
                        int firstEmpty = inv.firstEmpty();
                        ItemStack idIs = inv.getItem(i);
                        ItemStack bookIs = book.toItemStack();
                        if(idIs != null && idIs.getType() != Material.AIR && firstEmpty != -1) {
                            inv.setItem(firstEmpty, idIs);
                        }
                        inv.setItem(i, bookIs);
                    }
                    new UpdatePlayerLastViewedTask(playerName, sql,
                            new LastViewedData(lastBookId)).runTaskAsynchronously(plugin);
                }
            }.runTask(plugin);
        }
    }
}
