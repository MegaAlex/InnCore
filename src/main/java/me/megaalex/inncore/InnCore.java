package me.megaalex.inncore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import me.megaalex.inncore.bungee.BungeeManager;
import me.megaalex.inncore.chatsync.ChatSyncManager;
import me.megaalex.inncore.cmdrewrite.CmdRewriteManager;
import me.megaalex.inncore.command.CommandManager;
import me.megaalex.inncore.config.ConfigManager;
import me.megaalex.inncore.credits.CreditsManager;
import me.megaalex.inncore.data.DataManager;
import me.megaalex.inncore.database.DatabaseManager;
import me.megaalex.inncore.factionsmisc.FactionsMiscManager;
import me.megaalex.inncore.misc.MiscManager;
import me.megaalex.inncore.misc.maxbans.MaxbansManager;
import me.megaalex.inncore.news.NewsManager;
import me.megaalex.inncore.npc.NpcManager;
import me.megaalex.inncore.pvp.PvpManager;
import me.megaalex.inncore.sky.SkyBlockManager;
import me.megaalex.inncore.towns.TownsManager;
import me.megaalex.inncore.votes.VotesManager;

public class InnCore extends JavaPlugin {


    private static InnCore instance;
    private ConfigManager configManager;
    private DataManager dataManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private PvpManager pvpManager;
    private CreditsManager creditsManager;
    private NpcManager npcManager;
    private VaultManager vaultManager;
    private ChatSyncManager chatSyncManager;
    private CmdRewriteManager cmdRewriteManager;
    private FactionsMiscManager factionsMiscManager;
    private NewsManager newsManager;
    private MiscManager miscManager;
    private SkyBlockManager skyBlockManager;
    private MaxbansManager maxbansManager;
    private TownsManager townsManager;
    private VotesManager votesManager;
    private BungeeManager bungeeManager;

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
        dataManager = new DataManager();
        databaseManager = new DatabaseManager();
        vaultManager = new VaultManager();
        commandManager = new CommandManager();
        pvpManager = new PvpManager();
        creditsManager = new CreditsManager();
        npcManager = new NpcManager();
        chatSyncManager = new ChatSyncManager();
        cmdRewriteManager = new CmdRewriteManager();
        factionsMiscManager = new FactionsMiscManager();
        newsManager = new NewsManager();
        miscManager = new MiscManager();
        skyBlockManager = new SkyBlockManager();
        maxbansManager = new MaxbansManager();
        townsManager = new TownsManager();
        votesManager = new VotesManager();
        bungeeManager = new BungeeManager();

        managerList.add(configManager);
        managerList.add(dataManager);
        managerList.add(databaseManager);
        managerList.add(vaultManager);
        managerList.add(commandManager);
        managerList.add(pvpManager);
        managerList.add(creditsManager);
        managerList.add(npcManager);
        managerList.add(chatSyncManager);
        managerList.add(factionsMiscManager);
        managerList.add(newsManager);
        managerList.add(miscManager);
        managerList.add(skyBlockManager);
        managerList.add(maxbansManager);
        managerList.add(townsManager);
        managerList.add(votesManager);
        managerList.add(bungeeManager);

        managerList.add(cmdRewriteManager);

        // Enable managers
        configManager.onEnable();
        dataManager.onEnable();
        databaseManager.onEnable();
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
    }

    public boolean isManagerEnabled(Manager manager) {
        String configVar = manager.getEnableConfigName();
        return manager.isEnabled() || configVar == null || (getConfig().getBoolean(configVar, false));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PvpManager getPvpManager() {
        return pvpManager;
    }

    public CreditsManager getCreditsManager() {
        return creditsManager;
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

    public NewsManager getNewsManager() {
        return newsManager;
    }

    public MiscManager getMiscManager() {
        return miscManager;
    }

    public SkyBlockManager getSkyBlockManager() {
        return skyBlockManager;
    }

    public MaxbansManager getMaxbansManager() {
        return maxbansManager;
    }

    public TownsManager getTownsManager() {
        return townsManager;
    }

    public BungeeManager getBungeeManager() {
        return bungeeManager;
    }

    public FactionsMiscManager getFactionsMiscManager() {
        return factionsMiscManager;
    }
}
