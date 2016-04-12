/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.sky;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;
import me.megaalex.inncore.sky.data.FallDeathEntry;

public class SkyBlockSqlModule extends SqlModule {

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        if (con == null)
            return;
        String deathInvTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "inncore_fallInventories` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `username` varchar(20) NOT NULL,\n" +
                "  `inventory` mediumblob NOT NULL,\n" +
                "  `timestamp` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
        Statement stmt = null;
        try {
            stmt = con.con.createStatement();
            stmt.executeUpdate(deathInvTable);
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

    public void insertEntry(FallDeathEntry entry) {
        SqlConnection con = getConnection();
        if (con == null)
            return;
        String insertQuery = "INSERT INTO `" + con.prefix + "inncore_fallInventories` (`username`, `inventory`, `timestamp`) VALUES(?, ?, ?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.con.prepareStatement(insertQuery);
            stmt.setString(1, entry.getUsername());
            Object invData = InnCore.getInstance().getSkyBlockManager().getInventorySerilization(entry.getInventory());
            stmt.setObject(2, invData);
            stmt.setLong(3, entry.getTimestamp());
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
