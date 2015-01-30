package me.megaalex.inncore.credits;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.database.Sql;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class CreditsManager extends Manager {

    public static BigDecimal get(Player player) {
        return get(player.getName());
    }

    public static BigDecimal get(String player) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.getCredits(player);
    }


    public static boolean has(Player player, double amount) {
        return has(player.getName(), new BigDecimal(amount).setScale(2));
    }

    public static boolean has(String player, double amount) {
       return has(player, new BigDecimal(amount).setScale(2));
    }

    public static boolean has(Player player, BigDecimal amount) {
        return has(player.getName(), amount);
    }

    public static boolean has(String player, BigDecimal amount) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        BigDecimal playerCredits = sql.getCredits(player);
        return (playerCredits.compareTo(amount) > -1);
    }

    public static boolean deduct(Player player, double amount) {
        return deduct(player.getName(), new BigDecimal(amount).setScale(2));
    }

    public static boolean deduct(String player, double amount) {
        return deduct(player, new BigDecimal(amount).setScale(2));
    }

    public static boolean deduct(Player player, BigDecimal amount) {
        return deduct(player.getName(), amount);
    }

    public static boolean deduct(String player, BigDecimal amount) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return has(player, amount) &&
                sql.changeCredits(player,
                        amount.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
    }

    public static boolean grant(Player player, double amount) {
        return grant(player.getName(), new BigDecimal(amount).setScale(2));
    }

    public static boolean grant(String player, double amount) {
        return grant(player, new BigDecimal(amount).setScale(2));
    }

    public static boolean grant(Player player, BigDecimal amount) {
        return grant(player.getName(), amount);
    }

    public static boolean grant(String player, BigDecimal amount) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.changeCredits(player,
                        amount.setScale(2, BigDecimal.ROUND_HALF_UP).plus());
    }

    public static boolean set(Player player, double amount) {
        return set(player.getName(), new BigDecimal(amount).setScale(2));
    }

    public static boolean set(String player, double amount) {
        return set(player, new BigDecimal(amount).setScale(2));
    }

    public static boolean set(Player player, BigDecimal amount) {
        return grant(player.getName(), amount);
    }

    public static boolean set(String player, BigDecimal amount) {
        Sql sql = InnCore.getInstance().getDatabaseManager().getSql();
        return sql.setCredits(player,
                amount.setScale(2, BigDecimal.ROUND_HALF_UP).plus());
    }
}
