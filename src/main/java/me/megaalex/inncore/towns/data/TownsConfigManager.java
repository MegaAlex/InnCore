/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.file.ConfigFile;
import me.megaalex.inncore.towns.object.Rank;

public class TownsConfigManager {

    private static final Rank NULL_RANK = new Rank(-1, "unknown rank", 1, -1);
    private ConcurrentHashMap<Integer, Rank> ranks;
    private ConfigFile ranksConfig;
    private ConfigFile msgConfig;


    public void init() {
        ranksConfig = new ConfigFile("townRanks", true);
        msgConfig = new ConfigFile("townMsg", true);
        loadRanks();
    }

    private void loadRanks() {
        FileConfiguration config = ranksConfig.getConfig();
        ranks = new ConcurrentHashMap<>();
        for(String key : config.getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                String name = config.getString(key + ".name", "Town rank name");
                int minPlayers = config.getInt(key + ".minPlayers", 1);
                int maxPlayers = config.getInt(key + ".maxPlayers", -1);
                Rank rank = new Rank(id, name, minPlayers, maxPlayers);
                ranks.put(id, rank);
            } catch (NumberFormatException e) {
                InnCore.getInstance().getLogger().warning("Invalid town rank number!");
            }
        }
    }

    public Map<Integer, Rank> getRanks() {
        return ranks;
    }

    public Rank getRank(int id) {
        Rank rank = ranks.get(id);
        return rank == null ? NULL_RANK : rank;
    }

    public Rank getNextRank(int id) {
        return getRank(id + 1);
    }

    public HashMap<String, String> getMessages() {
        HashMap<String, String> messages = new HashMap<>();
        FileConfiguration config = msgConfig.getConfig();
        for(String key : config.getKeys(false)) {
            messages.put(key, config.getString(key));
        }
        return messages;
    }
}
