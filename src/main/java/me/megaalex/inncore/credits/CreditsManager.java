package me.megaalex.inncore.credits;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;

public class CreditsManager extends Manager {

    private CreditsSqlModule sql;

    @Override
    public void onEnable() {
        super.onEnable();

        sql = new CreditsSqlModule();
        InnCore.getInstance().getDatabaseManager().registerSqlModule(sql);
    }

    public static BigDecimal get(Player player) {
        return get(player.getName());
    }

    public static BigDecimal get(String player) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return sql.getCredits(player);
    }


    public static boolean has(Player player, double amount) {
        return has(player.getName(), new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean has(String player, double amount) {
       return has(player, new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean has(Player player, BigDecimal amount) {
        return has(player.getName(), amount);
    }

    public static boolean has(String player, BigDecimal amount) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        BigDecimal playerCredits = sql.getCredits(player);
        return (playerCredits.compareTo(amount) > -1);
    }

    public static boolean deduct(Player player, double amount) {
        return deduct(player.getName(), new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean deduct(String player, double amount) {
        return deduct(player, new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean deduct(Player player, BigDecimal amount) {
        return deduct(player.getName(), amount);
    }

    public static boolean deduct(String player, BigDecimal amount) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return has(player, amount) &&
                sql.changeCredits(player,
                        amount.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
    }

    public static boolean grant(Player player, double amount) {
        return grant(player.getName(), new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean grant(String player, double amount) {
        return grant(player, new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean grant(Player player, BigDecimal amount) {
        return grant(player.getName(), amount);
    }

    public static boolean grant(String player, BigDecimal amount) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return sql.changeCredits(player,
                        amount.setScale(2, BigDecimal.ROUND_HALF_UP).plus());
    }

    public static boolean set(Player player, double amount) {
        return set(player.getName(), new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean set(String player, double amount) {
        return set(player, new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean set(Player player, BigDecimal amount) {
        return grant(player.getName(), amount);
    }

    public static boolean set(String player, BigDecimal amount) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return sql.setCredits(player,
                amount.setScale(2, BigDecimal.ROUND_HALF_UP).plus());
    }

    public CreditsSqlModule getSql() {
        return sql;
    }

    public static boolean saveTransaction(TransactionData transactionData) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return sql.saveTransaction(transactionData);
    }

    public static List<CreditsData> getTopAccounts(int amount) {
        CreditsSqlModule sql = InnCore.getInstance().getCreditsManager().getSql();
        return sql.getCreditsTopList(amount);
    }
}
