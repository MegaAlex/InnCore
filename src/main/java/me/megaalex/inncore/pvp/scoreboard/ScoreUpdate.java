package me.megaalex.inncore.pvp.scoreboard;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.pvp.PvpSqlModule;

public class ScoreUpdate extends BukkitRunnable {

    @Override
    public void run() {
        final InnCore plugin = InnCore.getInstance();
        PvpSqlModule sql = plugin.getPvpManager().getSql();
        int players = plugin.getConfigManager().pvpConfig.getScorePlayers();
        final HashMap<String,Integer> topKillers = sql.getTopKillers(players);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getPvpManager().setTopScores(topKillers);
            }
        }.runTask(plugin);
    }
}
