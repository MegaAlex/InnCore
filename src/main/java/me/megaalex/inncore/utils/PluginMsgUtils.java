/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.megaalex.inncore.InnCore;

public class PluginMsgUtils {

    public static void sendMessage(byte[] data) {
        if(Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(InnCore.getInstance(), "InnCraft", data);
    }

    public static void connectPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(InnCore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
