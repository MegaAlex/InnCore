package me.megaalex.inncore.pvp.scoreboard;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreShowTask extends BukkitRunnable {
    @Override
    public void run() {
        PvpManager pvpManager = InnCore.getInstance().getPvpManager();
        for(Player player: Bukkit.getServer().getOnlinePlayers()) {
            pvpManager.showTopKillScore(player);
        }
    }
}
