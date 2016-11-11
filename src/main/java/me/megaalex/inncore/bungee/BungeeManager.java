/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.bungee;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class BungeeManager extends Manager {

    BungeeSqlModule sqlModule;

    @Override
    public String getEnableConfigName() {
        return "bungee.enabled";
    }

    @Override
    public void onEnable() {
        super.onEnable();

        sqlModule = new BungeeSqlModule();
        InnCore.getInstance().getDatabaseManager().registerSqlModule(sqlModule);
    }


    public void executeRemoteCmd(String cmd) {
        sqlModule.addRemoteCommand(cmd);
    }
}
