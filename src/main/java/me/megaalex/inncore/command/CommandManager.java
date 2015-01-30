package me.megaalex.inncore.command;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

import java.util.List;

public class CommandManager extends Manager {

    public CommandManager() {
        setExecutors();
    }

    private void setExecutors() {
        List<String> enabledCommands =
                InnCore.getInstance().getConfigManager().enabledCommands;
        if(enabledCommands.contains("credits")) {
            InnCore.getInstance().getCommand("credits").setExecutor(new CreditsCommand());
        }
        // TODO: Enable this and enable it in plugin.yml
        /*if(enabledCommands.contains("pvp")) {
            InnCore.getInstance().getCommand("pvp").setExecutor(new PvpCommand());
        }*/
    }
}
