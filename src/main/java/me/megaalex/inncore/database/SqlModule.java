/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.database;

import java.sql.Connection;
import java.sql.SQLException;

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
}
