/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.command.InnCoreCommand;
import me.megaalex.inncore.config.MiscConfig;
import me.megaalex.inncore.data.CustomDataFile;
import me.megaalex.inncore.utils.PluginMsgUtils;

public class MiscManager extends Manager {

    private MiscConfig config;
    private CustomDataFile miscDataFile;
    private Location spawnLocation;

    private Scoreboard collisionScoreboard;

    @Override
    public void onEnable() {
        super.onEnable();
        InnCore plugin = InnCore.getInstance();
        config = plugin.getConfigManager().getMiscConfig();
        miscDataFile = plugin.getDataManager().getConfigFile("miscData.yml");
        plugin.getServer().getPluginManager().registerEvents(new MiscListener(config), plugin);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "InnCraft");

        spawnLocation = loadSpawnLocation();

        if(config.disableCollision()) {
            collisionScoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
            Team colTeam = collisionScoreboard.getTeam("collision");
            if(colTeam == null) colTeam = collisionScoreboard.registerNewTeam("collision");
            Team.OptionStatus status;
            try {
                status = Team.OptionStatus.valueOf(config.getCollisonType().toUpperCase());
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Invalid collision type!");
                status = Team.OptionStatus.FOR_OWN_TEAM;
            }
            colTeam.setOption(Team.Option.COLLISION_RULE, status);
        }
    }

    public void handleServerCommand(Player player, String serverName) {
        serverName = serverName.toLowerCase();
        if(!config.getServerCmdList().contains(serverName)) {
            InnCoreCommand.sendError(player, "Server not found!");
            return;
        }
        if(!player.hasPermission("inncore.server.use." + serverName)) {
            InnCoreCommand.sendNoPerm(player);
            return;
        }
        PluginMsgUtils.connectPlayer(player, serverName);
    }

    private Location loadSpawnLocation() {
        FileConfiguration config = miscDataFile.getConfig();
        if(!config.contains("spawn.world") || !config.contains("spawn.x")
                || !config.contains("spawn.y") || !config.contains("spawn.z")) {
            return null;
        }
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        if(world == null) {
            return null;
        }
        return new Location(world, config.getInt("spawn.x"), config.getInt("spawn.y"),
                config.getInt("spawn.z"), (float) config.getDouble("spawn.yaw", 0),
                (float) config.getDouble("spawn.pitch", 0));
    }

    public void setSpawnLocation(Location location) {
        if(location == null || this.miscDataFile == null)
            return;
        final CustomDataFile customDataFile = this.miscDataFile;
        FileConfiguration config = customDataFile.getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getBlockX());
        config.set("spawn.y", location.getBlockY());
        config.set("spawn.z", location.getBlockZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());

        new BukkitRunnable() {
            @Override
            public void run() {
                customDataFile.saveConfig();
            }
        }.runTaskAsynchronously(InnCore.getInstance());
        spawnLocation = location;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public MiscConfig getConfig() {
        return config;
    }

    public void addPlayerToCollisionTeam(Player player) {
        if(collisionScoreboard == null || collisionScoreboard.getTeam("collision") == null) {
            return;
        }
        collisionScoreboard.getTeam("collision").addEntry(player.getName());
    }

    public void removePlayerFromCollisionTeam(Player player) {
        if(collisionScoreboard == null || collisionScoreboard.getTeam("collision") == null) {
            return;
        }
        collisionScoreboard.getTeam("collision").removeEntry(player.getName());
    }
}
