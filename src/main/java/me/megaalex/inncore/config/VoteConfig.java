/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class VoteConfig implements SubConfig {

    public List<String> services;
    public List<String> startCmd;
    public List<String> endCmd;

    public List<String> bungeeStartCmd;
    public List<String> bungeeEndCmd;

    public VoteConfig() {
        services = new ArrayList<>();
        startCmd = new ArrayList<>();
        endCmd = new ArrayList<>();
        bungeeStartCmd = new ArrayList<>();
        bungeeEndCmd = new ArrayList<>();
    }


    @Override
    public void loadConfig(ConfigurationSection config) {
        services = config.getStringList("services");
        startCmd = config.getStringList("startCmd");
        endCmd = config.getStringList("endCmd");

        bungeeStartCmd = config.getStringList("bungeeStartCmd");
        bungeeEndCmd = config.getStringList("bungeeEndCmd");
    }

    @Override
    public String getSubName() {
        return "vote";
    }
}
