package me.megaalex.inncore.pvp.scoreboard;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.database.Sql;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ScoreUpdate extends BukkitRunnable {

    @Override
    public void run() {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        int players = InnCore.getInstance().getConfigManager().pvpConfig.getScorePlayers();
        final HashMap<String,Integer> topKillers = sql.getTopKillers(players);
        Bukkit.getScheduler().runTask(InnCore.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                InnCore.getInstance().getPvpManager().setTopScores(topKillers);
            }
        });
    }
}
