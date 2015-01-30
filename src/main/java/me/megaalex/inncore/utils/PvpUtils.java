package me.megaalex.inncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Map;

public class PvpUtils {

    public static void showTopScoreFor(Player player, Scoreboard board) {
        if(board == null) {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        player.setScoreboard(board);
    }

    public static Scoreboard parseTopScoreboard(Map<String, Integer> topData) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective topObjective = board.registerNewObjective("topKills", "dummy");
        topObjective.setDisplayName(ChatColor.GREEN + "Top biqchi:");
        topObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for(Map.Entry<String, Integer> entry : topData.entrySet()) {
            topObjective.getScore(entry.getKey()).setScore(entry.getValue());
        }

        return board;
    }

    public static boolean isPvpScoreboard(Scoreboard score) {
        return score.getObjective("topKills") != null;
    }
}
