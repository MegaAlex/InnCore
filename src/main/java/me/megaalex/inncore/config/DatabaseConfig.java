/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

import me.megaalex.inncore.database.DatabaseData;

public class DatabaseConfig implements SubConfig {

    public DatabaseData globalData;
    public DatabaseData serverData;

    @Override
    public void loadConfig(ConfigurationSection config) {

        String globalDatabaseHost = config.getString("global.host", "localhost:3306");
        String globalDatabaseUser = config.getString("global.username", "root");
        String globalDatabasePass = config.getString("global.password", "pass");
        String globalDatabaseDb = config.getString("global.database", "db");
        String globalDatabasePrefix = config.getString("global.prefix", "");

        globalData = new DatabaseData(globalDatabaseHost, globalDatabaseUser, globalDatabasePass,
                globalDatabaseDb, globalDatabasePrefix);

        String serverDatabaseHost = config.getString("server.host", "localhost:3306");
        String serverDatabaseUser = config.getString("server.username", "root");
        String serverDatabasePass = config.getString("server.password", "pass");
        String serverDatabaseDb = config.getString("server.database", "db");
        String serverDatabasePrefix = config.getString("server.prefix", "");

        serverData = new DatabaseData(serverDatabaseHost, serverDatabaseUser, serverDatabasePass,
                serverDatabaseDb, serverDatabasePrefix);

    }

    @Override
    public String getSubName() {
        return "database";
    }
}
