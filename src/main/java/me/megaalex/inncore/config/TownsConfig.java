/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public class TownsConfig implements SubConfig {

    private String world;


    @Override
    public void loadConfig(ConfigurationSection config) {
        world = config.getString("world", "world");
    }

    @Override
    public String getSubName() {
        return "towns";
    }

    public String getWorld() {
        return world;
    }
}
