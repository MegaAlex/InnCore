/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.database;

public class DatabaseData {

    public final String databaseHost;
    public final String databaseUser;
    public final String databasePass;
    public final String databaseDb;
    public final String databasePrefix;

    public DatabaseData(String databaseHost, String databaseUser, String databasePass,
                        String databaseDb, String databasePrefix) {
        this.databaseHost = databaseHost;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        this.databaseDb = databaseDb;
        this.databasePrefix = databasePrefix;
    }
}
