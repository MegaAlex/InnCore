/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.towns.cmd.LotsCommandHandler;
import me.megaalex.inncore.towns.cmd.LotsCommandResponseManager;
import me.megaalex.inncore.towns.cmd.TownCommandHandler;
import me.megaalex.inncore.towns.cmd.TownCommandResponseManager;
import me.megaalex.inncore.towns.data.TownsConfigManager;
import me.megaalex.inncore.towns.data.TownsDataManager;

public class TownsManager extends Manager {

    private TownsSqlModule sqlModule;
    private TownsConfigManager config;
    private MessagesManager messageManager;
    private TownsDataManager data;
    private TownCommandHandler townCommand;
    private LotsCommandHandler lotsCommand;
    private TownCommandResponseManager townResponseManager;
    private LotsCommandResponseManager lotsResponseManager;



    @Override
    public String getEnableConfigName() {
        return "towns.enabled";
    }

    public TownsSqlModule getSqlModule() {
        return sqlModule;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        InnCore plugin = InnCore.getInstance();

        if(!plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            plugin.getLogger().warning("WorldGuard not found, disabling towns support");
            onDisable();
            return;
        }

        sqlModule = new TownsSqlModule();
        plugin.getDatabaseManager().registerSqlModule(sqlModule);

        config = new TownsConfigManager();
        config.init();

        messageManager = new MessagesManager(config.getMessages());

        data = new TownsDataManager(this);
        data.init();

        townResponseManager = new TownCommandResponseManager(this);
        lotsResponseManager = new LotsCommandResponseManager(this);
        townCommand = new TownCommandHandler(townResponseManager);
        lotsCommand = new LotsCommandHandler(lotsResponseManager);
        InnCoreCommand innCmd = plugin.getCommandManager().getInnCmd();
        innCmd.addHandler(townCommand);
        innCmd.addHandler(lotsCommand);
    }


    public MessagesManager getMessageManager() {
        return messageManager;
    }

    public TownsConfigManager getConfig() {
        return config;
    }

    public TownsDataManager getDataManager() {
        return data;
    }
}
