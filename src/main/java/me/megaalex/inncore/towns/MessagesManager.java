/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessagesManager {

    private HashMap<String, String> messages;

    public MessagesManager(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public void sendMessage(String sender, String msg, String... args) {
        if(sender == null || msg == null) {
            return;
        }
        sendMessage(Bukkit.getPlayer(sender), msg, args);
    }

    public void sendMessageAllOnline(String msg, String... args) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if(players == null || players.isEmpty() || msg == null) {
            return;
        }
        String formattedMsg = formatMessage(msg, args);
        for(Player player : players) {
            if(player != null)
                player.sendMessage(formattedMsg);
        }
    }

    public void sendMessageAll(Collection<String> players, String msg, String... args) {
        if(players == null || players.isEmpty() || msg == null) {
            return;
        }
        String formattedMsg = formatMessage(msg, args);
        for(String playerName : players) {
            Player player = Bukkit.getPlayer(playerName);
            if(player != null)
                player.sendMessage(formattedMsg);
        }
    }

    public void sendMessage(CommandSender sender, String msg, String... args) {
        if(sender == null || msg == null) {
            return;
        }
        String message = formatMessage(msg, args);
        sender.sendMessage(message);

    }

    public String formatMessage(String msg, String[] args) {
        String message = messages.get(msg);
        String prefix = messages.get("prefix");
        if(prefix == null) prefix = "";
        if(message == null) {
            message = msg;
        }
        int argNum = 1;
        for(String arg: args) {
            message = message.replaceAll("\\{" + argNum++ + "\\}", arg);
        }
        message = message.replaceAll("\\{prefix}", prefix);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
