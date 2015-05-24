package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public class ChatSyncConfig implements SubConfig {

    private String serverName;
    private String serverPass;

    // Chat sync proxy config config
    private String proxyHost;
    private int proxyPort;
    private String proxyPass;

    @Override
    public String getSubName() {
        return "chat.sync";
    }

    @Override
    public void loadConfig(final ConfigurationSection config) {

        this.serverName = config.getString("serverName", "serverName");
        this.serverPass = config.getString("serverPass", "123456");

        this.proxyHost = config.getString("connection.server", "localhost");
        this.proxyPort = config.getInt("connection.port", 8756);
        this.proxyPass = config.getString("connection.password", "123456");

    }

    public String getServerName() {
        return serverName;
    }

    public String getServerPass() {
        return serverPass;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyPass() {
        return proxyPass;
    }
}
