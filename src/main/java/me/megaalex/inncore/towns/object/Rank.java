/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.object;

public class Rank {
    private int id;
    private String name;
    private int minPlayers;
    private int maxPlayers;

    public Rank(int id, String name, int minPlayers, int maxPlayers) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinplayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMaxPlayersString() {
        return maxPlayers < 1 ? "bezkrai" : String.valueOf(maxPlayers);
    }

    public boolean isReal() {
        return id > 0;
    }
}
