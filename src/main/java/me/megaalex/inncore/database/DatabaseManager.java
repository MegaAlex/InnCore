package me.megaalex.inncore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.config.DatabaseConfig;

public class DatabaseManager extends Manager {

    private Set<SqlModule> sqlModuleList;

    @Override
    public void onEnable() {
        super.onEnable();
        sqlModuleList = new HashSet<>();
    }

    public SqlConnection getConnection(ConnectionType type) {

        DatabaseData data;
        DatabaseConfig config = InnCore.getInstance().getConfigManager().getDatabaseConfig();

        if(type == ConnectionType.GLOBAL) {
            data = config.globalData;
        } else {
            data = config.serverData;
        }

        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + data.databaseHost + "/"
                    + data.databaseDb, data.databaseUser, data.databasePass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return new SqlConnection(con, data.databasePrefix);
    }

    public void registerSqlModule(SqlModule module) {
        sqlModuleList.add(module);
        module.setupTables();
    }
}
