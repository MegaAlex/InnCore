package me.megaalex.inncore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import me.megaalex.inncore.chatsync.ChatSyncManager;
import me.megaalex.inncore.cmdrewrite.CmdRewriteManager;
import me.megaalex.inncore.command.CommandManager;
import me.megaalex.inncore.config.ConfigManager;
import me.megaalex.inncore.database.DatabaseManager;
import me.megaalex.inncore.factionsmisc.FactionsMiscManager;
import me.megaalex.inncore.npc.NpcManager;
import me.megaalex.inncore.pvp.PvpManager;

public class InnCore extends JavaPlugin {


    private static InnCore instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private PvpManager pvpManager;
    private NpcManager npcManager;
    private VaultManager vaultManager;
    private ChatSyncManager chatSyncManager;
    private CmdRewriteManager cmdRewriteManager;
    private FactionsMiscManager factionsMiscManager;

    private List<Manager> managerList;

    private static boolean debug;
    private static Logger logger;

    public static InnCore getInstance() {
        return InnCore.instance;
    }

    public void onEnable() {
        InnCore.instance = this;

        managerList = new ArrayList<>();

        // Initialize the managers
        configManager = new ConfigManager();
        databaseManager = new DatabaseManager();
        vaultManager = new VaultManager();
        commandManager = new CommandManager();
        pvpManager = new PvpManager();
        npcManager = new NpcManager();
        chatSyncManager = new ChatSyncManager();
        cmdRewriteManager = new CmdRewriteManager();
        factionsMiscManager = new FactionsMiscManager();

        managerList.add(configManager);
        managerList.add(databaseManager);
        managerList.add(vaultManager);
        managerList.add(commandManager);
        managerList.add(pvpManager);
        managerList.add(npcManager);
        managerList.add(chatSyncManager);
        managerList.add(factionsMiscManager);

        managerList.add(cmdRewriteManager);

        // Enable managers
        configManager.onEnable();
        databaseManager.onEnable();
        vaultManager.onEnable();
        commandManager.onEnable();
        enableManagers();

        InnCore.debug = configManager.debug;
        InnCore.logger = getLogger();
    }

    private void enableManagers() {
        for(Manager manager : managerList) {
            String configVar = manager.getEnableConfigName();
            if(manager.isEnabled() || (configVar != null && !getConfig().getBoolean(configVar, false))) {
                continue;
            }
            manager.onEnable();
        }
    }

    public void onDisable() {
        for(Manager manager : managerList) {
            if(manager.isEnabled()) {
                manager.onDisable();
            }
        }
        // TODO: Cleanup
        /*configManager.onDisable();
        databaseManager.onDisable();
        vaultManager.onDisable();
        commandManager.onDisable();
        cmdRewriteManager.onDisable();
        if(pvpManager.isEnabled()) {
            pvpManager.onDisable();
        }
        if(npcManager.isEnabled()) {
            npcManager.onDisable();
        }
        if(chatSyncManager.isEnabled()) {
            chatSyncManager.onDisable();
        }
        if(miningManager.isEnabled()) {
            miningManager.onDisable();
        }*/
    }

    public boolean isManagerEnabled(Manager manager) {
        String configVar = manager.getEnableConfigName();
        return manager.isEnabled() || configVar == null || (getConfig().getBoolean(configVar, false));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PvpManager getPvpManager() {
        return pvpManager;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public NpcManager getNpcManager() {
        return npcManager;
    }

    public ChatSyncManager getChatSyncManager() {
        return chatSyncManager;
    }

    public CmdRewriteManager getCmdRewriteManager() {
        return cmdRewriteManager;
    }

    public static void debug(String message) {
        if(InnCore.debug) {
            logger.info(message);
        }
    }

}
