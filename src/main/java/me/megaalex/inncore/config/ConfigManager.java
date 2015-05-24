package me.megaalex.inncore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class ConfigManager extends Manager {

    public List<String> enabledCommands;

    public String databaseHost;
    public String databaseUser;
    public String databasePass;
    public String databaseDb;
    public String databasePrefix;

    public PvpConfig pvpConfig;
    private ChatSyncConfig chatSyncConfig;
    private LoginLogConfig loginLogConfig;
    private FactionMiscConfig factionMiscConfig;

    private List<SubConfig> subConfigList;

    public List<String> npcPermittedGroups;

    public boolean debug;

    @Override
    public void onEnable() {
        super.onEnable();
        enabledCommands = new ArrayList<>();
        pvpConfig = new PvpConfig();
        chatSyncConfig = new ChatSyncConfig();
        factionMiscConfig = new FactionMiscConfig();

        subConfigList = new ArrayList<>();
        subConfigList.add(pvpConfig);
        subConfigList.add(chatSyncConfig);
        subConfigList.add(factionMiscConfig);
        loadConfig();
    }

    private void loadConfig() {
        final InnCore plugin = InnCore.getInstance();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        databaseHost = config.getString("database.host", "localhost:3306");
        databaseUser = config.getString("database.username", "root");
        databasePass = config.getString("database.password", "pass");
        databaseDb = config.getString("database.database", "db");
        databasePrefix = config.getString("database.prefix", "");

        debug = config.getBoolean("debug", false);

        // Load sub configs
        for(SubConfig subConfig : subConfigList) {
            ConfigurationSection configSection = config.getConfigurationSection(subConfig.getSubName());
            if (configSection == null) {
                configSection = config.createSection(subConfig.getSubName());
            }
            subConfig.loadConfig(configSection);
        }

        // Load enabled commands
        for(String key: config.getConfigurationSection("commands").getKeys(false)) {
            if(config.getBoolean("commands." + key, false)) {
                enabledCommands.add(key);
            }
        }
        // Load NPC config
        npcPermittedGroups = config.getStringList("npc.permittedGroups");
    }

    public ChatSyncConfig getChatSyncConfig() {
        return chatSyncConfig;
    }

    public FactionMiscConfig getFactionMiscConfig() {
        return factionMiscConfig;
    }

    public FileConfiguration getConfig() {
        return InnCore.getInstance().getConfig();
    }

    public LoginLogConfig getLoginLogConfig() {
        return loginLogConfig;
    }
}
