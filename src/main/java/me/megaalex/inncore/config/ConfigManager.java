package me.megaalex.inncore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class ConfigManager extends Manager {

    public List<String> enabledCommands;

    private DatabaseConfig databaseConfig;
    public PvpConfig pvpConfig;
    private ChatSyncConfig chatSyncConfig;
    private LoginLogConfig loginLogConfig;
    private FactionMiscConfig factionMiscConfig;
    private NpcConfig npcConfig;
    private MiscConfig miscConfig;
    private SkyBlockConfig skyConfig;
    private List<SubConfig> subConfigList;



    public boolean debug;

    @Override
    public void onEnable() {
        super.onEnable();
        enabledCommands = new ArrayList<>();
        databaseConfig = new DatabaseConfig();
        pvpConfig = new PvpConfig();
        chatSyncConfig = new ChatSyncConfig();
        factionMiscConfig = new FactionMiscConfig();
        npcConfig = new NpcConfig();
        miscConfig = new MiscConfig();
        skyConfig = new SkyBlockConfig();


        subConfigList = new ArrayList<>();
        subConfigList.add(databaseConfig);
        subConfigList.add(pvpConfig);
        subConfigList.add(chatSyncConfig);
        subConfigList.add(factionMiscConfig);
        subConfigList.add(npcConfig);
        subConfigList.add(miscConfig);
        subConfigList.add(skyConfig);
        loadConfig();
    }

    // Migrate config to a new version
    private void migrateConfig() {
        final InnCore plugin = InnCore.getInstance();
        FileConfiguration config = plugin.getConfig();

        // Migration 1: added global & server db settings
        if(config.contains("database.host")) {
            config.set("database.global.host", config.getString("database.host"));
            config.set("database.server.host", config.getString("database.host"));
            config.set("database.host", null);
        }
        if(config.contains("database.user")) {
            config.set("database.global.user", config.getString("database.user"));
            config.set("database.server.user", config.getString("database.user"));
            config.set("database.user", null);
        }
        if(config.contains("database.pass")) {
            config.set("database.global.pass", config.getString("database.pass"));
            config.set("database.server.pass", config.getString("database.pass"));
            config.set("database.pass", null);
        }
        if(config.contains("database.db")) {
            config.set("database.global.db", config.getString("database.db"));
            config.set("database.serve.db", config.getString("database.db"));
            config.set("database.db", null);
        }
        if(config.contains("database.prefix")) {
            config.set("database.server.prefix", config.getString("database.prefix"));
            config.set("database.db", null);
        }

        plugin.saveConfig();
        plugin.reloadConfig();
    }

    private void loadConfig() {
        final InnCore plugin = InnCore.getInstance();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        migrateConfig();

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

    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
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

    public NpcConfig getNpcConfig() {
        return npcConfig;
    }

    public MiscConfig getMiscConfig() {
        return miscConfig;
    }

    public SkyBlockConfig getSkyConfig() {
        return skyConfig;
    }

}
