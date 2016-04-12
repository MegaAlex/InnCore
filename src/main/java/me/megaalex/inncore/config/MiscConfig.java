/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

public class MiscConfig implements SubConfig {

    private boolean colorSigns;
    private boolean colorBooks;

    private Set<String> serverCmdList;

    private boolean tpOnFall;
    private List<String> fallWorlds;
    private boolean tpOnJoin;

    @Override
    public void loadConfig(ConfigurationSection config) {
        colorSigns = config.getBoolean("color.sign", true);
        colorBooks = config.getBoolean("color.book", false);

        serverCmdList = new HashSet<>();
        serverCmdList.addAll(config.getStringList("serverList"));

        tpOnFall = config.getBoolean("spawn.tpOnFall", false);
        fallWorlds = config.getStringList("spawn.worlds");
        tpOnJoin = config.getBoolean("spawn.tpOnJoin", false);

    }

    @Override
    public String getSubName() {
        return "misc";
    }

    public boolean colorSigns() {
        return colorSigns;
    }

    public boolean colorBooks() {
        return colorBooks;
    }

    public Set<String> getServerCmdList() {
        return serverCmdList;
    }

    public boolean tpOnFall() {
        return tpOnFall;
    }

    public List<String> getFallWorlds() {
        return fallWorlds;
    }

    public boolean tpOnJoin() {
        return tpOnJoin;
    }
}
