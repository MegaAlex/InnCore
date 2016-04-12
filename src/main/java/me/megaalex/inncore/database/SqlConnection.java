/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.database;

import java.sql.Connection;

public class SqlConnection {

    public Connection con;
    public String prefix;

    public SqlConnection(Connection con, String prefix) {
        this.con = con;
        this.prefix = prefix;
    }
}
