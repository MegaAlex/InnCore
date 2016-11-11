/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.megaalex.inncore.database.ConnectionType;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

class VotesSqlModule extends SqlModule {

    @Override
    public ConnectionType getDefaultType() {
        return ConnectionType.SERVER;
    }

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        if (con == null)
            return;
        String latestTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "votes_latest` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `name` varchar(32) NOT NULL,\n" +
                "  `service` varchar(20) NOT NULL,\n" +
                "  `time` int(11) NOT NULL,\n" +
                "  `processed` tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  UNIQUE KEY `name` (`name`,`service`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        try {
            stmt = con.con.prepareStatement(latestTable);
            stmt.executeUpdate();
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

    void addVote(PlayerVote vote) {
        PreparedStatement stmt = null;
        SqlConnection con = getConnection();
        if (con == null || vote == null)
            return;
        try {
            String query = "INSERT INTO `" + con.prefix + "votes_latest`(`name`, `service`, `time`) VALUES(?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE `time` = ?, `processed` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, vote.getName());
            stmt.setString(2, vote.getService());
            stmt.setLong(3, vote.getTime());
            stmt.setLong(4, vote.getTime());
            stmt.setBoolean(5, false);
            stmt.executeUpdate();
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

    List<String> getActivePlayerVoteServices(String player) {
        PreparedStatement stmt = null;
        SqlConnection con = getConnection();
        List<String> result = new ArrayList<>();
        if (con == null || player == null)
            return result;
        try {
            String query = "SELECT `service` FROM `" + con.prefix + "votes_latest` " +
                    "WHERE `name` like ? AND `time` > ?";
            stmt = con.con.prepareStatement(query);
            long time = getPastTime();
            stmt.setString(1, player);
            stmt.setLong(2, time);
            ResultSet rs =  stmt.executeQuery();
            while(rs.next()) {
                result.add(rs.getString("service"));
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
        return result;
    }

    private long getPastTime() {
        return System.currentTimeMillis() / 1000L - 86400;
    }

    List<PlayerVote> getInactiveVotes() {
        PreparedStatement stmt = null;
        SqlConnection con = getConnection();
        List<PlayerVote> result = new ArrayList<>();
        if (con == null)
            return result;
        try {
            String query = "SELECT `id`,`name`, `service`, `time` FROM `" + con.prefix + "votes_latest` " +
                    "WHERE `time` < ? AND `processed` = 0";
            stmt = con.con.prepareStatement(query);
            long time = getPastTime();
            stmt.setLong(1, time);
            ResultSet rs =  stmt.executeQuery();
            while(rs.next()) {
                PlayerVote vote = new PlayerVote(rs.getInt("id"), rs.getString("name"),
                        rs.getString("service"), rs.getLong("time"));
                result.add(vote);
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
        return result;
    }

    void updateProcessed(List<Integer> rowIds) {
        SqlConnection con = getConnection();
        if (con == null || rowIds == null || rowIds.isEmpty())
            return;
        List<String> batch = new ArrayList<>();
        for(Integer rowId : rowIds) {
            String query = "UPDATE `" + con.prefix + "votes_latest` SET `processed` = 1 WHERE `id` = " + rowId;
            batch.add(query);
        }

        executeBatchQuiet(con, batch.toArray(new String[batch.size()]));
        closeConnection(con);
    }
}
