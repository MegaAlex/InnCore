/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.paper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.megaalex.inncore.Manager;

public class PaperManager extends Manager implements Listener {

    private int viewDistance;

    @Override
    public void onEnable() {
        super.onEnable();

        viewDistance = Bukkit.getViewDistance();
    }

    @Override
    public String getEnableConfigName() {
        return "paper.enabled";
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int distance) {
        this.viewDistance = distance;
        setDistanceAllPlayers(distance);
    }

    private void setDistanceAllPlayers(int distance) {
        if(!isEnabled())
            return;
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setViewDistance(distance);
        }
    }

    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().setViewDistance(viewDistance);
    }
}
