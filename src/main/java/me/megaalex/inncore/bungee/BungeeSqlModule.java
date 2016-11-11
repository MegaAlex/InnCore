/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.bungee;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.megaalex.inncore.database.ConnectionType;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

class BungeeSqlModule extends SqlModule {

    @Override
    public ConnectionType getDefaultType() {
        return ConnectionType.GLOBAL;
    }


    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        if (con == null)
            return;
        String latestTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "bungee_remote_cmd` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `cmd` varchar(256) NOT NULL,\n" +
                "  `time` int(11) NOT NULL,\n" +
                "  `executed` tinyint(1) NOT NULL DEFAULT '0',\n" +
                "  PRIMARY KEY (`id`)\n" +
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

    void addRemoteCommand(String cmd) {
        PreparedStatement stmt = null;
        SqlConnection con = getConnection();
        if (con == null || cmd == null)
            return;
        try {
            String query = "INSERT INTO `" + con.prefix + "bungee_remote_cmd`(`cmd`, `time`) VALUES(?, ?)";
            stmt = con.con.prepareStatement(query);
            long time = System.currentTimeMillis() / 1000L;
            stmt.setString(1, cmd);
            stmt.setLong(2, time);
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
}
