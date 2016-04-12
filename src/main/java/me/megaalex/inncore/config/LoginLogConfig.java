package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

import me.megaalex.inncore.InnCore;

public class LoginLogConfig implements SubConfig {
    private boolean enabled;
    private boolean authLogEnabled;

    private String loginTable;
    private String authTable;

    @Override
    public String getSubName() {
        return "loginlog";
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        enabled = config.getBoolean("enabled", true);
        authLogEnabled = config.getBoolean("enabledAuthLog", false);
        loginTable = config.getString("loginTable", "inncore_logins");
        authTable = config.getString("authTable", "inncore_authentications");

    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAuthLogEnabled() {
        return authLogEnabled;
    }

    public String getLoginTable() {
        return getDBPrefix() + loginTable;
    }

    public String getAuthTable() {
        return getDBPrefix() + authTable;
    }

    private String getDBPrefix() {
        return InnCore.getInstance().getConfigManager().getDatabaseConfig().globalData.databasePrefix;
    }
}
