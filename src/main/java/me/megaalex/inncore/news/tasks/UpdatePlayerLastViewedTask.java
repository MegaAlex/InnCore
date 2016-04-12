/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.news.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.news.LastViewedData;
import me.megaalex.inncore.news.NewsSqlModule;

public class UpdatePlayerLastViewedTask extends BukkitRunnable {

    private final String playerName;
    private final NewsSqlModule sql;
    private final LastViewedData lastViewed;

    public UpdatePlayerLastViewedTask(String playerName, NewsSqlModule sql, LastViewedData lastViewed) {
        this.playerName = playerName;
        this.sql = sql;
        this.lastViewed = lastViewed;
    }

    @Override
    public void run() {
        sql.setLastViewed(playerName, lastViewed);
    }
}
