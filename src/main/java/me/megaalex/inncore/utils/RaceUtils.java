package me.megaalex.inncore.utils;

import org.bukkit.entity.Player;

public class RaceUtils {
    public static boolean hasRace(final Player player) {
        return player.hasPermission("inncore.race");
    }
}
