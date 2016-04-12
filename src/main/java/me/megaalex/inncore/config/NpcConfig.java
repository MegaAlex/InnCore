/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class NpcConfig implements SubConfig {

    public List<String> npcPermittedGroups;

    public boolean staffMemberEnabled;
    public double staffMemberYOffset;

    public String staffMemberOnlineText;
    public String staffMemberOfflineText;

    @Override
    public void loadConfig(ConfigurationSection config) {

        npcPermittedGroups = config.getStringList("permittedGroups");

        staffMemberEnabled = config.getBoolean("staffMember.enabled", true);
        staffMemberYOffset = config.getDouble("staffMember.yOffset", 3.5);

        staffMemberOnlineText = config.getString("staffMember.text.online", "&f[&aOnline&f]");
        staffMemberOfflineText = config.getString("staffMember.text.offline", "&f[&cOffline&f]");
    }

    @Override
    public String getSubName() {
        return "npc";
    }
}
