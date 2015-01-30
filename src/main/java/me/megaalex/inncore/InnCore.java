package me.megaalex.inncore;

import me.megaalex.inncore.command.CommandManager;
import me.megaalex.inncore.config.ConfigManager;
import me.megaalex.inncore.database.DatabaseManager;
import me.megaalex.inncore.pvp.PvpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InnCore extends JavaPlugin {


    private static InnCore instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private PvpManager pvpManager;

    public static InnCore getInstance() {
        return InnCore.instance;
    }

    public void onEnable() {
        InnCore.instance = this;

        // Initialize the managers
        configManager = new ConfigManager();
        databaseManager = new DatabaseManager();
        commandManager = new CommandManager();
        pvpManager = new PvpManager();

        // Register event listeners
        registerListeners();
    }

    public void onDisable() {
        configManager.onDisable();
        databaseManager.onDisable();
        commandManager.onDisable();
        pvpManager.onDisable();
    }

    private void registerListeners() {

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
}
