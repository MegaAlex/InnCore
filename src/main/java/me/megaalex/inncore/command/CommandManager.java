package me.megaalex.inncore.command;

import java.util.ArrayList;
import java.util.List;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.handlers.CreditsHandler;
import me.megaalex.inncore.command.handlers.InnCoreHandler;
import me.megaalex.inncore.command.handlers.NpcHandler;
import me.megaalex.inncore.command.handlers.PvpHandler;

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

        if(plugin.isManagerEnabled(plugin.getPvpManager()) && enabledCommands.contains("npc")) {
                handlers.add(new NpcHandler());
            }
            if(enabledCommands.contains("credits")) {
                handlers.add(new CreditsHandler());
            }
            if(enabledCommands.contains("pvp")) {
                handlers.add(new PvpHandler());
            }
        return handlers;
    }

    public InnCoreCommand getInnCmd() {
        return innCmd;
    }
}
