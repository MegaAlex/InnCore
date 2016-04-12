package me.megaalex.inncore.cmdrewrite;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.command.handlers.InnCoreHandler;

public class CmdRewriteManager extends Manager {

    public HashMap<String, String> rewiteCmds;
    private InnCoreCommand innCmd;

    @Override
    public String getEnableConfigName() {
        return "cmdrewrite.enabled";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        InnCore plugin = InnCore.getInstance();

        rewiteCmds = new HashMap<>();
        innCmd = plugin.getCommandManager().getInnCmd();
        loadRewrites();
        plugin.getServer().getPluginManager().registerEvents(new CmdRewriteListener(), plugin);
    }

    private void loadRewrites() {
        FileConfiguration config = InnCore.getInstance().getConfigManager().getConfig();
        for(String cmd : config.getConfigurationSection("cmdrewrite.rewrite").getKeys(false)) {
            String rewriteWith = config.getString("cmdrewrite.rewrite." + cmd, "false");
            rewiteCmds.put(cmd.toLowerCase(), rewriteWith.toLowerCase());
        }
    }

    public boolean processCommand(Player player, String message) {
        String[] splitMessage = message.substring(1).split(" ");
        String args[] = Arrays.copyOfRange(splitMessage, 1, splitMessage.length);
        String cmd = splitMessage[0];
        String handlerName = rewiteCmds.get(cmd);
        if(handlerName != null) {
            InnCoreHandler handler = innCmd.getHandler(handlerName);
            if(handler == null) {
                InnCore.getInstance().getLogger().warning("Couldn't find handler with name " + handlerName + ".");
                return false;
            }
            handler.handle(innCmd, player, cmd, args);
            return true;
        }
        return false;
    }
}
