package me.megaalex.inncore.pvp;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.database.Sql;
import me.megaalex.inncore.pvp.scoreboard.ScoreHideTask;
import me.megaalex.inncore.pvp.scoreboard.ScoreShowTask;
import me.megaalex.inncore.pvp.scoreboard.ScoreUpdate;
import me.megaalex.inncore.utils.PvpUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PvpManager extends Manager {

    public Scoreboard topBoard;
    private BukkitTask updateScoreTask;
    private BukkitTask showScoreTask;

    public Set<UUID> shownScoreboard;

    public void onEnable() {
        if(!InnCore.getInstance().getConfigManager().pvpConfig.isEnabled())
            return;
        updateScoreTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                InnCore.getInstance(), new ScoreUpdate(), 20L,
                InnCore.getInstance().getConfigManager().pvpConfig.getScoreUpdate());
        if(InnCore.getInstance().getConfigManager().pvpConfig.useScoreboard()) {
            long delay = InnCore.getInstance().getConfigManager().pvpConfig.getScoreDelay();
            showScoreTask = Bukkit.getScheduler().runTaskTimer(InnCore.getInstance(),
                    new ScoreShowTask(), delay, delay);
        }
        Bukkit.getPluginManager().registerEvents(new EventListener(),
                InnCore.getInstance());
        shownScoreboard = new HashSet<>();
    }

    public static int getKills(Player player) {
        return getKills(player.getName());
    }

    public static boolean setKills(Player player, int kills) {
        return setKills(player.getName(), kills);
    }

    public static int getKills(String player) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.getKills(player);
    }

    public static boolean addKill(Player player) {
        return addKill(player.getName());
    }

    public static boolean addKill(String player) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.addKills(player, 1);
    }

    public static boolean setKills(String player, int kills) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.setKills(player, kills);
    }

    public void showTopKillScore(Player player) {
        if(shownScoreboard.contains(player.getUniqueId()))
            return;
        //System.out.println(topBoard.toString());
        shownScoreboard.add(player.getUniqueId());
        PvpUtils.showTopScoreFor(player, topBoard);
        Bukkit.getScheduler().runTaskLater(InnCore.getInstance(),
                new ScoreHideTask(player.getUniqueId()),
                InnCore.getInstance().getConfigManager().pvpConfig.getScoreTime());
    }

    public void setTopScores(HashMap<String,Integer> topScores) {
        topBoard = PvpUtils.parseTopScoreboard(topScores);
    }

    @Override
    public void onDisable() {
        if(shownScoreboard == null || shownScoreboard.isEmpty())
            return;

        for(UUID playerId : shownScoreboard) {
            hideTopKillScore(playerId);
        }
    }

    public void hideTopKillScore(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        Set<UUID> shownScoreboard =
                InnCore.getInstance().getPvpManager().shownScoreboard;
        if(player == null || !shownScoreboard.contains(playerId))
            return;
        shownScoreboard.remove(playerId);
        if(PvpUtils.isPvpScoreboard(player.getScoreboard())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

}
