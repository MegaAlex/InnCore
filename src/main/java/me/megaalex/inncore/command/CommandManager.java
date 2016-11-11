package me.megaalex.inncore.command;

import java.util.ArrayList;
import java.util.List;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.handlers.CreditsHandler;
import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.command.handlers.NewsHandler;
import me.megaalex.inncore.command.handlers.NpcHandler;
import me.megaalex.inncore.command.handlers.PvpHandler;
import me.megaalex.inncore.command.handlers.StaffNpcHandler;
import me.megaalex.inncore.command.handlers.utils.GbroadcastHandler;
import me.megaalex.inncore.command.handlers.MiningHandler;
import me.megaalex.inncore.command.handlers.utils.ServerHandler;
import me.megaalex.inncore.command.handlers.utils.ServerTpHandler;
import me.megaalex.inncore.command.handlers.utils.SetSpawnHandler;
import me.megaalex.inncore.command.sky.handlers.SaplingHandler;

public class CommandManager extends Manager {

    private InnCoreCommand innCmd;

    @Override
    public void onEnable() {
        super.onEnable();
        innCmd = new InnCoreCommand();
        setExecutors();
    }

    private void setExecutors() {
        List<String> enabledCommands =
                InnCore.getInstance().getConfigManager().enabledCommands;
        if(enabledCommands.contains("inn")) {
            final List<InnCoreHandler> handlers = getInnHandlers();
            innCmd.addAllHandlers(handlers);

            InnCore.getInstance().getCommand("inn").setExecutor(innCmd);
        }
    }

    private List<InnCoreHandler> getInnHandlers() {
        InnCore plugin = InnCore.getInstance();
        List<String> enabledCommands = plugin.getConfigManager().enabledCommands;
        final List<InnCoreHandler> handlers = new ArrayList<>();

        if(plugin.isManagerEnabled(plugin.getNpcManager()) && enabledCommands.contains("npc")) {
            handlers.add(new NpcHandler());
            handlers.add(new StaffNpcHandler());
        }
        if(enabledCommands.contains("credits")) {
            handlers.add(new CreditsHandler());
        }
        if(enabledCommands.contains("pvp")) {
            handlers.add(new PvpHandler());
        }
        if(enabledCommands.contains("gbroadcast")) {
            handlers.add(new GbroadcastHandler());
        }
        if(enabledCommands.contains("stp")) {
            handlers.add(new ServerTpHandler());
        }
        if(enabledCommands.contains("server")) {
            handlers.add(new ServerHandler());
        }
        if(enabledCommands.contains("sapling")) {
            handlers.add(new SaplingHandler());
        }
        if(enabledCommands.contains("setspawn")) {
            handlers.add(new SetSpawnHandler());
        }
        if(enabledCommands.contains("mining")) {
            handlers.add(new MiningHandler());
        }


        if(plugin.isManagerEnabled(plugin.getNewsManager())) {
            handlers.add(new NewsHandler());
        }
        return handlers;
    }

    public InnCoreCommand getInnCmd() {
        return innCmd;
    }
}
