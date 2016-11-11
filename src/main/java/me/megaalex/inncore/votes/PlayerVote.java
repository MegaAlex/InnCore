/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.votes;

public class PlayerVote {

    private Integer id;
    private String name;
    private String service;
    private long time;

    public PlayerVote(Integer id, String name, String service, long time) {
        this.id = id;
        this.name = name;
        this.service = service;
        this.time = time;
    }

    public PlayerVote(String name, String service) {
        this.name = name;
        this.service = service;
        time = System.currentTimeMillis() / 1000L;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public long getTime() {
        return time;
    }
}
