package me.megaalex.inncore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.config.ConfigManager;

public class DatabaseManager extends Manager {

    private Connection con = null;

    public Sql getSql() {
        return new Sql(getConnection());
    }

    public Connection getConnection() {
        if(con == null) {
            con = getNewConnection();
        }

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeQuery("SELECT 1");
        }
        catch (SQLException e) {
            con = getNewConnection();
        }
        finally {
            try {
            if (stmt != null)
                stmt.close();
            } catch (SQLException ignored) {
            }
        }

        return con;
    }

    private Connection getNewConnection() {

        Connection con = null;
        try {
            ConfigManager config = InnCore.getInstance().getConfigManager();
            con = DriverManager.getConnection("jdbc:mysql://" + config.databaseHost + "/"
                    + config.databaseDb, config.databaseUser, config.databasePass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return con;
    }

    public void closeConnection(Connection conn) {

        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
