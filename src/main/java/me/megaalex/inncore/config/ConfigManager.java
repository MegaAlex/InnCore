package me.megaalex.inncore.config;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends Manager {

    public List<String> enabledCommands;

    public String databaseHost;
    public String databaseUser;
    public String databasePass;
    public String databaseDb;

    public PvpConfig pvpConfig;

    public ConfigManager() {
        enabledCommands = new ArrayList<>();
        pvpConfig = new PvpConfig();
        loadConfig();
    }

    private void loadConfig() {
        InnCore.getInstance().saveDefaultConfig();
        FileConfiguration config = InnCore.getInstance().getConfig();
        InnCore.getInstance().saveConfig();
        databaseHost = config.getString("database.host", "localhost:3306");
        databaseUser = config.getString("database.username", "root");
        databasePass = config.getString("database.password", "pass");
        databaseDb = config.getString("database.database", "db");

        // Load the pvp config
        loadPvpConfig();

        // Load enabled commands
        for(String key: config.getConfigurationSection("commands").getKeys(false)) {
            if(config.getBoolean("commands." + key, false)) {
                enabledCommands.add(key);
            }
        }
    }

    private void loadPvpConfig() {
        FileConfiguration config = InnCore.getInstance().getConfig();
        pvpConfig.setEnabled(config.getBoolean("pvp.enabled", false));
        pvpConfig.setCountTable(config.getString("pvp.killsTable", "inncore_kills"));
        pvpConfig.setCountTable(config.getString("pvp.killsTable", "inncore_kills-h"));


        // PvP Scoreboard Settings
        pvpConfig.setUseScoreboard(config.getBoolean("pvp.scoreboard.use", false));
        pvpConfig.setScoreDelay(config.getLong("pvp.scoreboard.delay", 6000L));
        pvpConfig.setScoreTime(config.getLong("pvp.scoreboard.time", 200L));
        pvpConfig.setScoreUpdate(config.getLong("pvp.scoreboard.update", 3600L));
        pvpConfig.setScorePlayers(config.getInt("pvp.scoreboard.players", 10));
    }
}
