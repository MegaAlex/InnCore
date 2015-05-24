/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import org.bukkit.configuration.ConfigurationSection;

public interface SubConfig {

    public void loadConfig(ConfigurationSection config);


    public String getSubName();
}
