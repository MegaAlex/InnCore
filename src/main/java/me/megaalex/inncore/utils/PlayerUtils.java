package me.megaalex.inncore.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;

public class PlayerUtils {


    public static Player getPlayer(String name) {
        return name == null ? null : Bukkit.getPlayerExact(name);
    }

    public static Player getPlayer(UUID id) {
        return id == null ? null : Bukkit.getPlayer(id);
    }

    public static void sendMessage(UUID playerId, Message type, String... args ) {
        Player player = getPlayer(playerId);

        if(player == null) {
            return;
        }

        MessageUtils.sendMsg(player, type, args);
    }

    public static void sendMessage(String playerName, Message type, String... args) {
        Player player = getPlayer(playerName);

        if(player == null) {
            return;
        }

        MessageUtils.sendMsg(player, type, args);
    }

    public static void sendMessage(Player player, String message, String... args) {
        if(player == null || !player.isOnline() || message == null || message.isEmpty()) {
            return;
        }
        message = MessageUtils.formatArgs(message, args);
        message = MessageUtils.formatMessage(message);
        player.sendMessage(message);
    }

    public static void sendMessageAllWithPermExcept(UUID playerId, String permission, String message, String... args) {
        if(message == null || message.isEmpty()) {
            return;
        }
        for(final Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission(permission) && !player.getUniqueId().equals(playerId)) {
                sendMessage(player, message, args);
            }
        }
    }

    public static String getNameWithPrefix(String player) {
        // TODO: Return the name with a prefix (red or gray? for the race)
        return player;
    }

    public static boolean isOnline(String name) {
        return getPlayer(name) != null;
    }

    public static void teleport(Player player, Location location) {
        if(player == null || location == null) {
            return;
        }

        player.teleport(location.clone().add(0, 1, 0));
        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0F);
    }
}
