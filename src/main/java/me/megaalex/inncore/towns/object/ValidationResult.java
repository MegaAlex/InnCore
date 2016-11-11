/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.object;

import org.bukkit.OfflinePlayer;

public class ValidationResult {
    public boolean success;
    public Town town;
    public OfflinePlayer offlinePlayer;

    public ValidationResult(boolean success, Town town, OfflinePlayer offlinePlayer) {
        this.success = success;
        this.town = town;
        this.offlinePlayer = offlinePlayer;
    }

    public ValidationResult(boolean success) {
        this.success = success;
    }
}