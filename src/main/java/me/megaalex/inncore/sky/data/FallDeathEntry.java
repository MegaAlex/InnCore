/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.sky.data;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class FallDeathEntry {

    private Integer id = null;
    private String username;
    private List<ItemStack> inventory;
    private long timestamp;

    public FallDeathEntry(String username, List<ItemStack> inventory) {
        this.username = username;
        this.inventory = inventory;

        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    public FallDeathEntry(String username, List<ItemStack> inventory, long timestamp) {
        this.username = username;
        this.inventory = inventory;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<ItemStack> getInventory() {
        return inventory;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
