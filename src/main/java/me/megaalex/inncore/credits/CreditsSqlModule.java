/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.credits;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.megaalex.inncore.database.ConnectionType;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

public class CreditsSqlModule extends SqlModule {

    @Override
    public ConnectionType getDefaultType() {
        return ConnectionType.GLOBAL;
    }

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        if (con == null)
            return;
        String creditsTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "donation_credits` (" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT," +
                "  `user` varchar(32) NOT NULL," +
                "  `credits` decimal(10,2) NOT NULL DEFAULT '0.00'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `user` (`user`)" +
                ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;";

        String transactionTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "donation_credits_transactions` (" +
                "  `id` int(5) NOT NULL AUTO_INCREMENT," +
                "  `sender` varchar(32) NOT NULL," +
                "  `receiver` varchar(32) NOT NULL," +
                "  `credits` decimal(10,2) NOT NULL DEFAULT '0.00'," +
                "  `time` int(10) NOT NULL," +
                "  `type` TINYINT(3) NOT NULL," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;";

        executeBatchQuiet(con, creditsTable, transactionTable);
        closeConnection(con);
    }


    public void createAccount(String player) {
        // This is the same as adding, as it is inserting
        changeCredits(player,
                BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public BigDecimal getCredits(String player) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        BigDecimal credits = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        try {
            String query = "SELECT `credits` FROM `" + con.prefix + "donation_credits` " +
                    "WHERE`user` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, player);
            result = stmt.executeQuery();
            while(result.next()) {
                credits = result.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return credits;
    }

    public boolean setCredits(String player, BigDecimal credits) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        boolean result = true;
        try {
            String query = "INSERT INTO `" + con.prefix + "donation_credits`(`user`, `credits`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `credits` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setBigDecimal(2, credits);
            stmt.setBigDecimal(3, credits);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return result;
    }

    public boolean changeCredits(String player, BigDecimal change) {
        BigDecimal currentCredits = getCredits(player);
        change = change.add(currentCredits);
        return setCredits(player, change);
    }

    public boolean saveTransaction(final TransactionData transaction) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        boolean result = true;
        try {
            String query = "INSERT INTO `" + con.prefix + "donation_credits_transactions`(`sender`, `receiver`, `credits`, `time`, `type`) VALUES(?, ?, ?, ?, ?)";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, transaction.getSender());
            stmt.setString(2, transaction.getReceiver());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setLong(4, transaction.getTime());
            stmt.setInt(5, transaction.getType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return result;
    }

    public List<CreditsData> getCreditsTopList(int playerAmount) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result;
        List<CreditsData> data = new ArrayList<>();
        if(playerAmount < 0) {
            return data;
        }
        try {
            String query = "SELECT `user`,`credits` FROM `" + con.prefix + "donation_credits` " +
                    "ORDER BY `credits` DESC LIMIT 0," + playerAmount;
            stmt = con.con.prepareStatement(query);
            result = stmt.executeQuery();
            while(result.next()) {
                String player = result.getString(1);
                BigDecimal credits = result.getBigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP);
                data.add(new CreditsData(player, credits));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        closeConnection(con);
        return data;
    }
}
