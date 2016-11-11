/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.megaalex.inncore.towns.data.TownsDataManager;
import me.megaalex.inncore.towns.object.Town;
import me.megaalex.inncore.towns.object.ValidationResult;
import me.megaalex.inncore.utils.UUIDUtils;

public class CommandResponse {

    protected MessagesManager msgManager;
    protected TownsDataManager dataManager;
    protected TownsManager townsManager;

    public CommandResponse(TownsManager townsManager) {
        this.townsManager = townsManager;
        msgManager = townsManager.getMessageManager();
        dataManager = townsManager.getDataManager();
    }

    protected ValidationResult validateMain(CommandSender sender, String player, String townName, String permSelf, String permAdmin) {

        Town town;
        boolean curT = townName == null;
        String perm = curT ? permSelf : permAdmin;
        if(!sender.hasPermission(perm)) {
            msgManager.sendMessage(sender, "error_noperms");
            return new ValidationResult(false);
        }
        town = curT ? dataManager.getPlayerTown(sender.getName())
                : dataManager.getTownByName(townName);
        if(town == null) {
            String noTownMsg = curT ? "error_not_in_town" : "error_no_town";
            msgManager.sendMessage(sender, noTownMsg);
            return new ValidationResult(false);
        }
        OfflinePlayer offlinePlayer = null;
        if(player != null) {
            UUID playerId = UUIDUtils.getPlayerId(player);
            Player onlinePlayer = Bukkit.getPlayer(playerId);
            if(onlinePlayer != null) {
                offlinePlayer = onlinePlayer;
            } else offlinePlayer = Bukkit.getOfflinePlayer(playerId);
            if (onlinePlayer == null && !offlinePlayer.hasPlayedBefore()) {
                msgManager.sendMessage(sender, "error_unknown_player");
                return new ValidationResult(false);
            }
        }
        return new ValidationResult(true, town, offlinePlayer);
    }
}
