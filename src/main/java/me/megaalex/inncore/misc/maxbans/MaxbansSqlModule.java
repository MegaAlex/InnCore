/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.misc.maxbans;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.maxgamer.maxbans.events.PunishEvent;

import me.megaalex.inncore.database.ConnectionType;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;

public class MaxbansSqlModule extends SqlModule {

    @Override
    public ConnectionType getDefaultType() {
        return ConnectionType.GLOBAL;
    }

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        if (con == null)
            return;
        String punishTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "punishHistory` (\n" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT,\n" +
                "  `event` varchar(20) DEFAULT NULL,\n" +
                "  `banner` varchar(20) DEFAULT NULL,\n" +
                "  `reason` varchar(128) DEFAULT NULL,\n" +
                "  `time` int(15) NOT NULL,\n" +
                "  `name` varchar(20) DEFAULT NULL,\n" +
                "  `ip` varchar(20) DEFAULT NULL,\n" +
                "  `expire` int(15) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
        Statement stmt = null;
        try {
            stmt = con.con.createStatement();
            stmt.executeUpdate(punishTable);
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

    public void saveEntry(PunishEvent event) {
        SqlConnection con = getConnection();
        if (con == null)
            return;

        String insertQuery = "INSERT INTO `" + con.prefix + "punishHistory`(`event`, `banner`, `reason`, `time`, `name`, `ip`, `expire`) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = con.con.prepareStatement(insertQuery);
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getBanner());
            stmt.setString(3, event.getReason());
            stmt.setLong(4, event.getTime());
            stmt.setString(5, event.getName());
            stmt.setString(6, event.getIp());
            stmt.setObject(7, event.getExpire());
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
