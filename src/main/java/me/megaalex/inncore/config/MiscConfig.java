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

    private boolean disableCollision;
    private String collisonType;

    @Override
    public void loadConfig(ConfigurationSection config) {
        colorSigns = config.getBoolean("color.sign", true);
        colorBooks = config.getBoolean("color.book", false);

        serverCmdList = new HashSet<>();
        serverCmdList.addAll(config.getStringList("serverList"));

        tpOnFall = config.getBoolean("spawn.tpOnFall", false);
        fallWorlds = config.getStringList("spawn.worlds");
        tpOnJoin = config.getBoolean("spawn.tpOnJoin", false);

        disableCollision = config.getBoolean("disablePlayerColission", false);
        collisonType = config.getString("collisionType", "FOR_OWN_TEAM");
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

    public boolean disableCollision() {
        return disableCollision;
    }

    public String getCollisonType() {
        return collisonType;
    }
}
