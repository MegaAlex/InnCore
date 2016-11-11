/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

public class VoteExpiryCheckTask extends BukkitRunnable {

    VotesManager manager;

    public VoteExpiryCheckTask(VotesManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        List<PlayerVote> expiredVotes = manager.sqlModule.getInactiveVotes();
        if(expiredVotes.isEmpty()) {
            return;
        }
        List<Integer> updateIds = new ArrayList<>();
        for(PlayerVote vote : expiredVotes) {
            manager.runEndCmdTask(vote.getName());
            updateIds.add(vote.getId());
        }
        manager.sqlModule.updateProcessed(updateIds);
    }
}
