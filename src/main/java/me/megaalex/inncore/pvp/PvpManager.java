package me.megaalex.inncore.pvp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.pvp.scoreboard.ScoreHideTask;
import me.megaalex.inncore.pvp.scoreboard.ScoreShowTask;
import me.megaalex.inncore.pvp.scoreboard.ScoreUpdate;
import me.megaalex.inncore.utils.PvpUtils;

public class PvpManager extends Manager {

    public Scoreboard topBoard;
    private BukkitTask updateScoreTask;
    private BukkitTask showScoreTask;

    public Set<UUID> shownScoreboard;

    private PvpSqlModule sql;

    @Override
    public String getEnableConfigName() {
        return "pvp.enabled";
    }

    public void onEnable() {
        super.onEnable();
        InnCore plugin = InnCore.getInstance();
        sql = new PvpSqlModule();
        plugin.getDatabaseManager().registerSqlModule(sql);
        updateScoreTask = new ScoreUpdate().runTaskTimerAsynchronously(
                plugin, 20L, plugin.getConfigManager().pvpConfig.getScoreUpdate());
        if(InnCore.getInstance().getConfigManager().pvpConfig.useScoreboard()) {
            long delay = plugin.getConfigManager().pvpConfig.getScoreDelay();
            showScoreTask = new ScoreShowTask().runTaskTimer(plugin, delay, delay);
        }
        Bukkit.getPluginManager().registerEvents(new EventListener(), plugin);
        shownScoreboard = new HashSet<>();
    }

    public static int getKills(Player player) {
        return getKills(player.getName());
    }

    public static boolean setKills(Player player, int kills) {
        return setKills(player.getName(), kills);
    }

    public static int getKills(String player) {
        PvpSqlModule sql = InnCore.getInstance().getPvpManager().getSql();
        return sql.getKills(player);
    }

    public static boolean addKill(Player player) {
        return addKill(player.getName());
    }

    public static boolean addKill(String player) {
        PvpSqlModule sql = InnCore.getInstance().getPvpManager().getSql();
        return sql.addKills(player, 1);
    }

    public static boolean setKills(String player, int kills) {
        PvpSqlModule sql = InnCore.getInstance().getPvpManager().getSql();
        return sql.setKills(player, kills);
    }

    public void showTopKillScore(Player player) {
        if(shownScoreboard.contains(player.getUniqueId()))
            return;
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
        if(isEnabled()) {
            if (shownScoreboard == null || shownScoreboard.isEmpty())
                return;

            for (UUID playerId : shownScoreboard) {
                hideTopKillScore(playerId);
            }
        }
        super.onDisable();
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

    public BukkitTask getUpdateScoreTask() {
        return updateScoreTask;
    }

    public BukkitTask getShowScoreTask() {
        return showScoreTask;
    }

    public PvpSqlModule getSql() {
        return sql;
    }
}
