/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.sky;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.config.SkyBlockConfig;
import me.megaalex.inncore.utils.SerializationUtil;

public class SkyBlockManager extends Manager {

    private SkyBlockSqlModule skySqlModule;
    private SkyBlockConfig skyConfig;

    @Override
    public void onEnable() {
        super.onEnable();

        InnCore plugin = InnCore.getInstance();
        this.skyConfig = plugin.getConfigManager().getSkyConfig();

        Bukkit.getPluginManager().registerEvents(new TeamChatListener(this), plugin);


        if(skyConfig.saveInvOnFallDeath()) {
            skySqlModule = new SkyBlockSqlModule();
            plugin.getDatabaseManager().registerSqlModule(skySqlModule);
            Bukkit.getPluginManager().registerEvents(new PlayerFallListener(this), plugin);
        }
    }

    @Override
    public String getEnableConfigName() {
        return "skyblock.enabled";
    }

    public List<UUID> getTeamMembers(UUID playerId) {
        List<UUID> teamMembers = Collections.emptyList();
        ASkyBlockAPI sbApi = ASkyBlockAPI.getInstance();
        if(sbApi != null && sbApi.inTeam(playerId)) {
            return sbApi.getTeamMembers(playerId);
        }
        return teamMembers;
    }

    public int getPlayerIslandLevel(UUID playerId) {
        ASkyBlockAPI sbApi = ASkyBlockAPI.getInstance();
        if(sbApi == null) {
            return 0;
        }

        if(sbApi.inTeam(playerId)) {
            UUID newId = sbApi.getTeamLeader(playerId);
            if(newId != null) {
                playerId = newId;
            }
        }
        return sbApi.hasIsland(playerId) ? sbApi.getIslandLevel(playerId) : 0;
    }

    public SkyBlockConfig getConfig() {
        return skyConfig;
    }

    public SkyBlockSqlModule getSql() {
        return skySqlModule;
    }

    public Object getInventorySerilization(List<ItemStack> inv) {
        return SerializationUtil.serializeItemList(inv);
    }
}
