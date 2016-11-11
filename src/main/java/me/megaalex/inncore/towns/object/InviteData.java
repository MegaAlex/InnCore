/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.object;

public class InviteData {

    private Town town;
    private String inviter;
    private long time;

    public InviteData(Town town, String inviter) {
        this.town = town;
        this.inviter = inviter;
        this.time = System.currentTimeMillis() / 1000L;
    }


    public Town getTown() {
        return town;
    }

    public String getInviter() {
        return inviter;
    }

    public long getTime() {
        return time;
    }

    public boolean isActive() {
        long curTime = System.currentTimeMillis() / 1000L;
        return time + 120L >= curTime;
    }
}
