package me.megaalex.inncore.loginlog;

import java.sql.Connection;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.LoginLogConfig;

public class LoginLogSqlManager {

    private Connection con;

    public LoginLogSqlManager(Connection con) {
        this.con = con;
        setupTables();
    }

    public void setupTables() {
        LoginLogConfig config = InnCore.getInstance().getConfigManager().getLoginLogConfig();
        String loginTable = "CREATE TABLE IF NOT EXISTS `" + config.getLoginTable() +
                "` (" +
                "  `id` int(5) NOT NULL AUTO_INCREMENT," +
                "  `user` varchar(32) NOT NULL," +
                "  `credits` decimal(10,2) NOT NULL DEFAULT '0.00'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `user` (`user`)" +
                ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;";
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}
