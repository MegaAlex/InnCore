/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.VotifierEvent;

public class VotesListner implements Listener {

    VotesManager manager;

    public VotesListner(VotesManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerVote(VotifierEvent e) {
        manager.onVote(e.getVote());
    }
}
