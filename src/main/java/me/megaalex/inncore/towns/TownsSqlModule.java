/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import me.megaalex.inncore.database.ConnectionType;
import me.megaalex.inncore.database.SqlConnection;
import me.megaalex.inncore.database.SqlModule;
import me.megaalex.inncore.towns.object.Town;

public class TownsSqlModule extends SqlModule {

    @Override
    public ConnectionType getDefaultType() {
        return ConnectionType.SERVER;
    }

    @Override
    public void setupTables() {
        SqlConnection con = getConnection();
        if (con == null)
            return;
        String townsTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "towns_towns` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(32) NOT NULL,\n" +
                "  `nameLong` varchar(64) NOT NULL,\n" +
                "  `mayor` varchar(20) NOT NULL,\n" +
                "  `description` varchar(256) DEFAULT NULL,\n" +
                "  `deleted` int(11) NOT NULL DEFAULT '0',\n" +
                "  `rank` int(11) NOT NULL DEFAULT '1',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `name` (`name`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
        String deputyTable = "CREATE TABLE IF NOT EXISTS `" + con.prefix + "towns_deputies` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(20) NOT NULL,\n" +
                "  `townId` int(11) NOT NULL\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String memberTable = "\n" +
                "CREATE TABLE IF NOT EXISTS `" + con.prefix + "towns_members` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(20) NOT NULL,\n" +
                "  `townId` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
        executeBatchQuiet(con, townsTable, deputyTable, memberTable);
        closeConnection(con);
    }

    /**
     * '
     * @param town the town
     * @return the id of the created town
     */
    public Integer saveCreateTown(Town town) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        Integer result = null;
        try {
            String query = "INSERT INTO `" + con.prefix + "towns_towns`(`name`, `nameLong`, `description`, `mayor`) VALUES(?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `name` = ?, `nameLong` = ?, `description` = ?, `mayor` = ?, `rank` = ?, `deleted` = ?";
            stmt = con.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, town.getName());
            stmt.setString(2, town.getNameLong());
            stmt.setString(3, town.getDescription());
            stmt.setString(4, town.getMayor());
            stmt.setString(5, town.getName());
            stmt.setString(6, town.getNameLong());
            stmt.setString(7, town.getDescription());
            stmt.setString(8, town.getMayor());
            stmt.setObject(9, town.getRank());
            stmt.setBoolean(10, town.isDeleted());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = null;
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
        return result;
    }

    public void deleteTown(int id) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE `" + con.prefix + "towns_towns` SET `deleted` = ? " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setBoolean(1, true);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public void updateMayor(int id, String mayor) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE `" + con.prefix + "towns_towns` SET `mayor` = ? " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, mayor);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public void updateNameLong(int id, String nameLong) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE `" + con.prefix + "towns_towns` SET `nameLong` = ? " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nameLong);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public Set<String> getMembers(int id) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        Set<String> members = new HashSet<>();

        try {
            String query = "SELECT `name` FROM `" + con.prefix + "towns_members` " +
                    "WHERE `townId` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                String player = result.getString(1);
                members.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
        return members;
    }

    public Set<String> getDeputies(int id) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        Set<String> members = new HashSet<>();

        try {
            String query = "SELECT `name` FROM `" + con.prefix + "towns_deputies` " +
                    "WHERE `townId` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                String player = result.getString(1);
                members.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
        return members;
    }

    private Town loadTown(Integer id, ResultSet result) {
        String name;
        try {
            name = result.getString("name");
            String nameLong = result.getString("nameLong");
            String description = result.getString("description");
            String mayor = result.getString("mayor");
            Integer rank = (Integer) result.getObject("rank");
            return new Town(id, name, nameLong, description, mayor, rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Town getTown(int id) {
        Town town = null;

        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "SELECT * FROM `" + con.prefix + "towns_towns` " +
                    "WHERE `id` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                town = loadTown(id, result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);

        if(town != null) {
            town.setMembers(getMembers(id));
            town.setDeputies(getDeputies(id));
        }

        return town;
    }

    public void addMember(String member, Town town) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO `" + con.prefix + "towns_members`(`name`, `townId`) VALUES(?, ?)";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, member);
            stmt.setInt(2, town.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public void addDeputy(String deputy, Town town) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "INSERT INTO `" + con.prefix + "towns_deputies`(`name`, `townId`) VALUES(?, ?)";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, deputy);
            stmt.setInt(2, town.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public void removeDeputy(String deputy, Town town) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM `" + con.prefix + "towns_deputies` WHERE `name` = ? AND `townId` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, deputy);
            stmt.setInt(2, town.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }

    public void removeMember(String member, Town town) {
        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "DELETE FROM `" + con.prefix + "towns_members` WHERE `name` = ? AND `townId` = ?";
            stmt = con.con.prepareStatement(query);
            stmt.setString(1, member);
            stmt.setInt(2, town.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);
    }



    public Set<Town> getActiveTowns() {
        Set<Town> towns = new HashSet<>();

        SqlConnection con = getConnection();
        PreparedStatement stmt = null;
        try {
            String query = "SELECT * FROM `" + con.prefix + "towns_towns` " +
                    "WHERE `deleted` = 0";
            stmt = con.con.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                Integer id = result.getInt("id");
                Town town = loadTown(id, result);
                if(town != null) {
                    towns.add(town);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatementQuiet(stmt);
        }
        closeConnection(con);

        for(Town town : towns) {
            town.setMembers(getMembers(town.getId()));
            town.setDeputies(getDeputies(town.getId()));
        }

        return towns;
    }

    private void closeStatementQuiet(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
