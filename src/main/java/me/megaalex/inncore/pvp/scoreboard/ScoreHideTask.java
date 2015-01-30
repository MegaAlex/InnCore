package me.megaalex.inncore.pvp.scoreboard;

import me.megaalex.inncore.InnCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ScoreHideTask extends BukkitRunnable {

    private final UUID playerId;

    public ScoreHideTask(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public void run() {
        InnCore.getInstance().getPvpManager().hideTopKillScore(playerId);
    }
}
