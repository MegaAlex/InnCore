/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public class SkyBlockConfig implements SubConfig {

    private boolean saveInvOnFallDeath;
    private String fallInvTable;

    @Override
    public void loadConfig(ConfigurationSection config) {
        saveInvOnFallDeath = config.getBoolean("fall.saveInv", false);
        fallInvTable = config.getString("fall.table", "inncore_fallInventories");
    }

    @Override
    public String getSubName() {
        return "skyblock";
    }

    public boolean saveInvOnFallDeath() {
        return saveInvOnFallDeath;
    }

    public String getFallInvTable() {
        return fallInvTable;
    }
}
