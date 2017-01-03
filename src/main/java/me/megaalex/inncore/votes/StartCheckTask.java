/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;

public class StartCheckTask extends BukkitRunnable {

    private List<String> receivedVotesUsernames;


    public StartCheckTask() {
        receivedVotesUsernames = new CopyOnWriteArrayList<>();
    }

    public void run() {
        VotesManager manager = InnCore.getInstance().getVotesManager();

        for(String username : receivedVotesUsernames) {
            List<String> activeVotes = manager.sqlModule.getActivePlayerVoteServices(username);
            if(activeVotes.size() == manager.getServicesSize()) {
                manager.runStartCmdTask(username);
            }
        }
        receivedVotesUsernames.clear();
    }

    public void addVote(String username) {
        receivedVotesUsernames.add(username);
    }

}
