/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class UUIDUtils {

    public static UUID getPlayerId(String player) {
        try {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.toLowerCase()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.toLowerCase()).getBytes());
    }
}
