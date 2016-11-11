/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.megaalex.inncore.InnCore;

public abstract class SqlModule {

    public void setupTables() {
    }

    public SqlConnection getConnection() {
        return getConnection(getDefaultType());
    }

    public SqlConnection getConnection(ConnectionType type) {
        return InnCore.getInstance().getDatabaseManager().getConnection(type);
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(SqlConnection con) {
        closeConnection(con.con);
    }

    public ConnectionType getDefaultType() {
        return ConnectionType.SERVER;
    }

    public int[] executeBatchQuiet(SqlConnection con, String... queries) {
        Statement stmt = null;
        int[] result = new int[queries.length];
        try {
            stmt = con.con.createStatement();
            for(final String query : queries) {
                stmt.addBatch(query);
            }
            result = stmt.executeBatch();
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
        return result;
    }
}
