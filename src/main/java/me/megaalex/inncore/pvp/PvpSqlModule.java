/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.PvpConfig;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

public class PvpSqlModule extends SqlModule {

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        PvpConfig pvpConfig = InnCore.getInstance().getConfigManager().pvpConfig;
        String pvpCountTable = "CREATE TABLE IF NOT EXISTS `" +
                con.prefix + pvpConfig.getCountTable() + "` (" +
                "  `id` int(7) NOT NULL AUTO_INCREMENT," +
                "  `user` varchar(32) NOT NULL," +
                "  `kills` int(7) NOT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `user` (`user`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

        String pvpHistoryTable = "CREATE TABLE IF NOT EXISTS `" +
                con.prefix + pvpConfig.getHistoryTable() + "` (" +
                "  `id` int(9) NOT NULL AUTO_INCREMENT," +
                "  `killer` varchar(32) NOT NULL," +
                "  `victim` varchar(32) NOT NULL," +
                "  `item` varchar(128) NOT NULL," +
                "  `time` int(12) NOT NULL," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1";

        if (con == null)
            return;

        Statement stmt = null;
        try {
            stmt = con.con.createStatement();
            stmt.addBatch(pvpCountTable);
            stmt.addBatch(pvpHistoryTable);
            stmt.executeBatch();
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
    }

    public int getKills(String player) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        String tableName = con.prefix + InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        int kills = 0;
        try {
            String query = "SELECT `kills` FROM `" + tableName + "` " +
                    "WHERE`user` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, player);
            result = stmt.executeQuery();
            while(result.next()) {
                kills = result.getInt(1);
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
        return kills;
    }

    public boolean setKills(String player, int kills) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = con.prefix + InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`user`, `kills`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `kills` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setInt(2, kills);
            stmt.setInt(3, kills);
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

    public boolean addKills(String player, int kills) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = con.prefix + InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`user`, `kills`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `kills` = `kills` + ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setInt(2, kills);
            stmt.setInt(3, kills);
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

    public boolean addKillHistory(String killer, String victim, String item, long time) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = con.prefix + InnCore.getInstance().getConfigManager().pvpConfig.getHistoryTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`killer`, `victim`, `item`, `time`) " +
                    "VALUES(?, ?, ?, ?)";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, killer);
            stmt.setString(2, victim);
            stmt.setString(3, item);
            stmt.setLong(4, time);
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

    public HashMap<String, Integer> getTopKillers(int number) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        ResultSet result = null;
        String tableName = con.prefix + InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        HashMap<String, Integer> topKillers = new HashMap<>();
        try {
            String query = " SELECT `user`, `kills` " +
                    "FROM `" + tableName + "` ORDER BY `kills` DESC " +
                    "LIMIT 0 , " + number;
            stmt = con.con.prepareStatement(query);
            //stmt.setString(1, tableName);
            //stmt.setInt(2, number);
            result = stmt.executeQuery();
            while(result.next()) {
                topKillers.put(result.getString(1), result.getInt(2));
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
        return topKillers;
    }
}
