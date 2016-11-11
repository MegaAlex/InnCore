/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.towns.TownsManager;
import me.megaalex.inncore.towns.object.Town;

public class TownsDataManager {

    private TownsManager townsManager;

    private ConcurrentHashMap<String, Town> towns;
    private ConcurrentHashMap<String, Town> userTowns;

    public TownsDataManager(TownsManager townsManager) {
        this.townsManager = townsManager;
    }

    public void init() {
        loadTowns();
    }

    public void addDeputy(final Town town, final String deputy) {
        if(town.isMayor(deputy)) {
            return;
        }

        town.removeMember(deputy);
        town.addDeputy(deputy);
        setPlayerTown(deputy, town);
        new BukkitRunnable() {
            @Override
            public void run() {
                townsManager.getSqlModule().removeMember(deputy, town);
                townsManager.getSqlModule().addDeputy(deputy, town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void addMember(final Town town, final String member) {
        if(town.isMayor(member)) {
            return;
        }
        removeDeputy(town, member);
        town.addMember(member);
        setPlayerTown(member, town);
        new BukkitRunnable() {
            @Override
            public void run() {
                townsManager.getSqlModule().addMember(member, town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void removeDeputy(final Town town, final String deputy) {
        if(!town.isDeputy(deputy)) {
            return;
        }
        town.removeDeputy(deputy);
        userTowns.remove(deputy.toLowerCase());
        new BukkitRunnable() {
            @Override
            public void run() {
                townsManager.getSqlModule().removeDeputy(deputy, town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void demoteDeputy(final Town town, final String deputy) {
        if(!town.isDeputy(deputy)) {
            return;
        }
        town.removeDeputy(deputy);
        town.addMember(deputy);
        new BukkitRunnable() {

            @Override
            public void run() {
                townsManager.getSqlModule().removeDeputy(deputy, town);
                townsManager.getSqlModule().addMember(deputy, town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void removeMember(final Town town, final String member) {
        if(!town.isMember(member)) {
            return;
        }
        town.removeMember(member);
        removePlayerTown(member);
        new BukkitRunnable() {
            @Override
            public void run() {
                townsManager.getSqlModule().removeMember(member, town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void setMayor(Town town, String mayor) {
        if(town.isDeputy(mayor)) {
            removeDeputy(town, mayor);
        } else if(town.isMember(mayor)) {
            removeMember(town, mayor);
        }
        addMember(town, town.getMayor());
        town.setMayor(mayor);
        setPlayerTown(mayor, town);
        saveTown(town);
    }

    public void setName(Town town, String name) {
        towns.remove(town.getName());
        town.setName(name);
        towns.put(town.getName().toLowerCase(), town);
        saveTown(town);
    }

    public void setNameLong(Town town, String nameLong) {
        town.setNameLong(nameLong);
        saveTown(town);
    }

    public void setDescription(Town town, String description) {
        town.setDescription(description);
        saveTown(town);
    }

    public void setRank(Town town, Integer rank) {
        town.setRank(rank);
        saveTown(town);
    }


    public void saveTown(final Town town) {
        new BukkitRunnable() {
            @Override
            public void run() {
                townsManager.getSqlModule().saveCreateTown(town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public void setPlayerTown(String player, Town town) {
        userTowns.put(player.toLowerCase(), town);
    }

    public void removePlayerTown(String player) {
        userTowns.remove(player.toLowerCase());
    }


    private void loadTowns() {
        towns = new ConcurrentHashMap<>();
        userTowns = new ConcurrentHashMap<>();
        for(Town town : townsManager.getSqlModule().getActiveTowns()) {
            towns.put(town.getName().toLowerCase(), town);
        }
        for(Map.Entry<String, Town> entry : towns.entrySet()) {
            for(String player : entry.getValue().getAllMembers()) {
                userTowns.put(player.toLowerCase(), entry.getValue());
            }
        }
    }


    public Town getPlayerTown(String player) {
        return userTowns.get(player.toLowerCase());
    }

    public Town getTownByName(String townName) {
        return towns.get(townName.toLowerCase());
    }

    /**
     * Creates a town
     * blocking function
     * @param town the town to be created
     * @return the id of the town; -1 on error
     */
    public int createTown(Town town) {
        if(town == null || town.getName() == null || town.getNameLong() == null || town.getMayor() == null) {
            return -1;
        }

        Integer id = townsManager.getSqlModule().saveCreateTown(town);
        if(id == null) {
            return -1;
        }
        towns.put(town.getName().toLowerCase(), town);
        town.setId(id);

        for(String name : town.getAllMembers()) {
            setPlayerTown(name, town);
        }

        return id;
    }

    public void deleteTown(final Town town) {
        if(town == null || town.getName() == null) {
            return;
        }
        Set<String> deputies = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Set<String> members = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        deputies.addAll(town.getDeputies());
        members.addAll(town.getMembers());

        for(String deputyName : deputies) {
            removeDeputy(town, deputyName);
        }
        for(String memberName : members) {
            removeMember(town, memberName);
        }
        removePlayerTown(town.getMayor());
        town.setDeleted(true);
        towns.remove(town.getName().toLowerCase());
        new BukkitRunnable(){
            @Override
            public void run() {
                saveTown(town);
            }
        }.runTaskAsynchronously(InnCore.getInstance());
    }

    public List<Town> getAllTowns() {
        List<Town> townList = new ArrayList<>();
        for(Map.Entry<String, Town> entry : towns.entrySet()) {
            townList.add(entry.getValue());
        }
        return townList;
    }
}
