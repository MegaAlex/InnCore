/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.object;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Town {

    private Integer id;
    private String name;
    private String nameLong;
    private String description;
    private String mayor;
    private Integer rank;
    private boolean deleted;
    private Set<String> deputies;
    private Set<String> members;

    public Town(String name, String nameLong, String mayor, int rank) {
        init(name);
        this.nameLong = nameLong;
        this.mayor = mayor;
        this.rank = rank;
    }

    private void init(String name) {
        this.name = name;
        this.id = null;
        this.nameLong = name;
        this.description = null;
        this.mayor = null;
        this.rank = null;
        deleted = false;
        this.deputies = new HashSet<>();
        this.members = new HashSet<>();
    }

    public Town(String name) {
        init(name);
    }

    public Town(Integer id, String name, String nameLong, String description, String mayor, Integer rank) {
        init(name);
        this.id = id;
        this.nameLong = nameLong;
        this.description = description;
        this.mayor = mayor;
        this.rank = rank;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLong() {
        return nameLong;
    }

    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMayor() {
        return mayor;
    }

    public void setMayor(String mayor) {
        this.mayor = mayor;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<String> getDeputies() {
        return deputies;
    }

    public void setDeputies(Set<String> deputies) {
        this.deputies = getNewPlayerSet();
        this.deputies.addAll(deputies);
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = getNewPlayerSet();
        this.members.addAll(members);
    }

    public boolean isMember(String player) {
        return members.contains(player);
    }

    public boolean isDeputy(String deputy) {
        return deputies.contains(deputy);
    }

    public boolean isMayor(String mayor) {
        return mayor.equalsIgnoreCase(this.mayor);
    }

    public boolean isInTown(String player) {
        return isDeputy(player) || isMember(player) || isMayor(player);
    }

    public Set<String> getAllMembers() {
        Set<String> all = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        all.addAll(deputies);
        all.addAll(members);
        all.add(mayor);
        return all;
    }

    public void addDeputy(String deputy) {
        deputies.add(deputy);
    }

    public void addMember(String member) {
        members.add(member);
    }

    public void removeDeputy(String deputy) {
        deputies.remove(deputy);
    }

    public void removeMember(String member) {
        members.remove(member);
    }

    private Set<String> getNewPlayerSet() {
        return new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }
}
