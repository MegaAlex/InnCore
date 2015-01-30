package me.megaalex.inncore.database;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.ConfigManager;
import me.megaalex.inncore.config.PvpConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;

public class Sql {

    private Connection con;

    public Sql(Connection con) {
        this.con = con;
        setupTables();
    }

    private void setupTables() {

        ConfigManager configManager = InnCore.getInstance().getConfigManager();
        PvpConfig pvpConfig = InnCore.getInstance().getConfigManager().pvpConfig;

        String creditsTable = "CREATE TABLE IF NOT EXISTS `donation_credits` (" +
                "  `id` int(5) NOT NULL AUTO_INCREMENT," +
                "  `user` varchar(32) NOT NULL," +
                "  `credits` decimal(10,2) NOT NULL DEFAULT '0.00'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `user` (`user`)" +
                ") ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;";

        String pvpCountTable = "CREATE TABLE IF NOT EXISTS `" +
                pvpConfig.getCountTable() + "` (" +
                "  `id` int(7) NOT NULL AUTO_INCREMENT," +
                "  `user` varchar(32) NOT NULL," +
                "  `kills` int(7) NOT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `user` (`user`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

        String pvpHistoryTable = "CREATE TABLE IF NOT EXISTS `" +
                pvpConfig.getHistoryTable() + "` (" +
                "  `id` int(9) NOT NULL AUTO_INCREMENT," +
                "  `killer` varchar(32) NOT NULL," +
                "  `victim` varchar(32) NOT NULL," +
                "  `item` varchar(128) NOT NULL," +
                "  `time` int(12) NOT NULL," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1";

        if (con == null)
            return;

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            if(configManager.enabledCommands.contains("credits")) {
                stmt.addBatch(creditsTable);
            }

            if(pvpConfig.isEnabled()) {
                stmt.addBatch(pvpCountTable);
                stmt.addBatch(pvpHistoryTable);
            }

            stmt.executeBatch();
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
    }


    public void createAccount(String player) {
        // This is the same as adding, as it is inserting
        changeCredits(player,
                BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public BigDecimal getCredits(String player) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        BigDecimal credits = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        try {
            String query = "SELECT `credits` FROM `donation_credits` " +
                    "WHERE`user` = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, player);
           result = stmt.executeQuery();
           while(result.next()) {
               credits = result.getBigDecimal(1);
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

        return credits;
    }

    public boolean setCredits(String player, BigDecimal credits) {
        PreparedStatement stmt = null;
        boolean result = true;
        try {
            String query = "INSERT INTO `donation_credits`(`user`, `credits`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `credits` = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setBigDecimal(2, credits);
            stmt.setBigDecimal(3, credits);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
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

    public boolean changeCredits(String player, BigDecimal change) {
        BigDecimal currentCredits = getCredits(player);
        change = change.add(currentCredits);
        return setCredits(player, change);
    }

    public int getKills(String player) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String tableName = InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        int kills = 0;
        try {
            String query = "SELECT `kills` FROM `" + tableName + "` " +
                    "WHERE`user` = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, player);
            result = stmt.executeQuery();
            while(result.next()) {
                kills = result.getInt(1);
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

        return kills;
    }

    public boolean setKills(String player, int kills) {
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`user`, `kills`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `kills` = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setInt(2, kills);
            stmt.setInt(3, kills);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
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

    public boolean addKills(String player, int kills) {
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`user`, `kills`) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE `kills` = `kills` + ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, player);
            stmt.setInt(2, kills);
            stmt.setInt(3, kills);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
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

    public boolean addKillHistory(String killer, String victim, String item, long time) {
        PreparedStatement stmt = null;
        boolean result = true;
        String tableName = InnCore.getInstance().getConfigManager().pvpConfig.getHistoryTable();
        try {
            String query = "INSERT INTO `" + tableName + "`(`killer`, `victim`, `item`, `time`) " +
                    "VALUES(?, ?, ?, ?)";
            stmt = con.prepareStatement(query);
            stmt.setString(1, killer);
            stmt.setString(2, victim);
            stmt.setString(3, item);
            stmt.setLong(4, time);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
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

    public HashMap<String, Integer> getTopKillers(int number) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String tableName = InnCore.getInstance().getConfigManager().pvpConfig.getCountTable();
        HashMap<String, Integer> topKillers = new HashMap<>();
        try {
            String query = " SELECT `user`, `kills` " +
                    "FROM `" + tableName + "` ORDER BY `kills` DESC " +
                    "LIMIT 0 , " + number;
            stmt = con.prepareStatement(query);
            //stmt.setString(1, tableName);
            //stmt.setInt(2, number);
            result = stmt.executeQuery();
            while(result.next()) {
                topKillers.put(result.getString(1), result.getInt(2));
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

        return topKillers;
    }

}
