/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.vexsoftware.votifier.model.Vote;
import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.bungee.BungeeManager;
import me.megaalex.inncore.config.VoteConfig;

public class VotesManager extends Manager {

    VotesSqlModule sqlModule;
    StartCheckTask startCheckTask;

    @Override
    public void onEnable() {
        super.onEnable();

        if(!Bukkit.getPluginManager().isPluginEnabled("Votifier")) {
            Bukkit.getLogger().warning("Votifier not found, votes manager disabled");
            onDisable();
            return;
        }

        InnCore plugin = InnCore.getInstance();
        sqlModule = new VotesSqlModule();
        InnCore.getInstance().getDatabaseManager().registerSqlModule(sqlModule);
        Bukkit.getPluginManager().registerEvents(new VotesListner(this), plugin);

        new VoteExpiryCheckTask(this).runTaskTimerAsynchronously(plugin, 600L, 600L);

        startCheckTask = new StartCheckTask();
        startCheckTask.runTaskTimerAsynchronously(plugin, 600L, 600L);
    }

    @Override
    public String getEnableConfigName() {
        return "vote.enabled";
    }

    public void onVote(final Vote vote) {
        final List<String> services = getServices();
        if(!services.contains(vote.getServiceName())) {
            Bukkit.getLogger().warning("Vote from unknown service relieved! Ignoring.");
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                sqlModule.addVote(new PlayerVote(vote.getUsername(), vote.getServiceName()));
                startCheckTask.addVote(vote.getUsername());
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    protected void runStartCmdTask(final String username) {
        VoteConfig voteConfig = InnCore.getInstance().getConfigManager().getVoteConfig();
        runCmds(username, voteConfig.startCmd);
        if(!voteConfig.bungeeStartCmd.isEmpty())
            runCmdsBungee(username, voteConfig.bungeeStartCmd);
    }
    void runEndCmdTask(final String username) {
        VoteConfig voteConfig = InnCore.getInstance().getConfigManager().getVoteConfig();
        runCmds(username, voteConfig.endCmd);
        if(!voteConfig.bungeeEndCmd.isEmpty())
            runCmdsBungee(username, voteConfig.bungeeEndCmd);
    }

    private void runCmds(final String username, final List<String> cmds) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(String cmd : cmds) {
                    cmd = cmd.replaceAll("\\{player\\}", username);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }.runTask(InnCore.getInstance());
    }

    private void runCmdsBungee(String username, List<String> cmds) {
        BungeeManager manager = InnCore.getInstance().getBungeeManager();
        if(!manager.isEnabled()) {
            Bukkit.getLogger().info("Bungee manager disabled! Ignoring bungee vote commands!");
            return;
        }
        for(String cmd : cmds) {
            cmd = cmd.replaceAll("\\{player\\}", username);
            manager.executeRemoteCmd(cmd);
        }
    }

    protected int getServicesSize() {
        return getServices().size();
    }

    private List<String> getServices() {
        final InnCore plugin = InnCore.getInstance();
        return plugin.getConfigManager().getVoteConfig().services;
    }
}
