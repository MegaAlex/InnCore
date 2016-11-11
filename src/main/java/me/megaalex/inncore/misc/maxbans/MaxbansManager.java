/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.misc.maxbans;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class MaxbansManager extends Manager {

    private MaxbansSqlModule maxbansSqlModule;

    @Override
    public void onEnable() {
        super.onEnable();

        InnCore plugin = InnCore.getInstance();

        maxbansSqlModule = new MaxbansSqlModule();
        plugin.getDatabaseManager().registerSqlModule(maxbansSqlModule);

        plugin.getServer().getPluginManager().registerEvents(new PunishListener(), plugin);
    }

    @Override
    public String getEnableConfigName() {
        return "misc.maxbans.enabled";
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public MaxbansSqlModule getMaxbansSqlModule() {
        return maxbansSqlModule;
    }
}
