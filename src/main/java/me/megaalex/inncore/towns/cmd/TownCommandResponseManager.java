/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.towns.cmd;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.towns.CommandResponse;
import me.megaalex.inncore.towns.TownsManager;
import me.megaalex.inncore.towns.object.InviteData;
import me.megaalex.inncore.towns.object.Rank;
import me.megaalex.inncore.towns.object.Town;
import me.megaalex.inncore.towns.object.ValidationResult;
import me.megaalex.inncore.utils.MessageUtils;
import me.megaalex.inncore.utils.UUIDUtils;

public class TownCommandResponseManager extends CommandResponse {

    private HashMap<String, InviteData> invitations;

    public TownCommandResponseManager(TownsManager townsManager) {
        super(townsManager);
        this.invitations = new HashMap<>();
    }

    public void infoCmd(CommandSender sender, String townName) {
        Town town;
        boolean curT = townName == null;
        String perm = curT ? "inntown.info.self" : "inntown.info.others";
        if(!sender.hasPermission(perm)) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        town = curT ? dataManager.getPlayerTown(sender.getName())
                : dataManager.getTownByName(townName);
        if(town == null) {
            String noTownMsg = curT ? "error_not_in_town" : "error_no_town";
            msgManager.sendMessage(sender, noTownMsg);
            return;
        }

        Rank townRank = townsManager.getConfig().getRank(town.getRank());
        msgManager.sendMessage(sender, "info_head", town.getNameLong(), townRank.getName(), String.valueOf(townRank.getId()));
        msgManager.sendMessage(sender, "info_name", town.getName());
        msgManager.sendMessage(sender, "info_desc", town.getDescription() == null ? "nqma opisanie" : town.getDescription());
        msgManager.sendMessage(sender, "info_players", String.valueOf(town.getAllMembers().size()), townRank.getMaxPlayersString());
        msgManager.sendMessage(sender, "info_mayor", town.getMayor());
        msgManager.sendMessage(sender, "info_deputies", MessageUtils.getSeparatedString(town.getDeputies(), "&2, &f"));
        msgManager.sendMessage(sender, "info_members", MessageUtils.getSeparatedString(town.getMembers(), "&2, &f"));
    }

    public void addDeputy(CommandSender sender, String player, String townName) {
        ValidationResult validate = validateMain(sender, player, townName, "inntown.deputy", "inntown.admin.deputy");
        if(!validate.success) {
            return;
        }

        boolean curT = townName == null;
        Town town = validate.town;
        OfflinePlayer offlinePlayer = validate.offlinePlayer;

        if(curT && !town.isMayor(sender.getName())) {
            msgManager.sendMessage(sender, "error_deputy_not_mayor");
            return;
        }

        if(town.isDeputy(player)) {
            msgManager.sendMessage(sender, "error_deputy_already_deputy");
            return;
        }

        if(!town.isMember(player)) {
            msgManager.sendMessage(sender, "error_not_member", player);
            return;
        }

        townsManager.getDataManager().addDeputy(town, offlinePlayer.getName());

        msgManager.sendMessage(sender, "deputy_added", offlinePlayer.getName());
        msgManager.sendMessageAll(town.getAllMembers(), "ann_deputy_added", offlinePlayer.getName());
    }

    public void unDeputy(CommandSender sender, String player, String townName) {
        ValidationResult validate = validateMain(sender, player, townName, "inntown.deputy", "inntown.admin.deputy");
        if(!validate.success) {
            return;
        }

        boolean curT = townName == null;
        Town town = validate.town;
        OfflinePlayer offlinePlayer = validate.offlinePlayer;

        if(curT && !town.isMayor(sender.getName())) {
            msgManager.sendMessage(sender, "error_deputy_not_mayor");
            return;
        }

        if(!town.isDeputy(player)) {
            msgManager.sendMessage(sender, "error_not_deputy");
            return;
        }
        townsManager.getDataManager().demoteDeputy(town, offlinePlayer.getName());
        msgManager.sendMessage(sender, "deputy_demoted", offlinePlayer.getName());
        msgManager.sendMessageAll(town.getAllMembers(), "ann_deputy_removed", offlinePlayer.getName());
    }

    public void setMayor(CommandSender sender, String player, String townName) {
        ValidationResult validate = validateMain(sender, player, townName, "inntown.mayor", "inntown.admin.mayor");
        boolean curT = townName == null;
        Town town = validate.town;
        OfflinePlayer offlinePlayer = validate.offlinePlayer;
        if(!validate.success) {
            return;
        }
        if(curT && !town.isMayor(sender.getName())) {
            msgManager.sendMessage(sender, "error_not_mayor");
            return;
        }
        if(curT && player.equalsIgnoreCase(sender.getName())) {
            msgManager.sendMessage(sender, "error_same_person");
            return;
        }

        if(!town.isInTown(player)) {
            msgManager.sendMessage(sender, "error_not_member", player);
            return;
        }
        townsManager.getDataManager().setMayor(town, offlinePlayer.getName());
        msgManager.sendMessage(sender, "mayor_changed", offlinePlayer.getName(), town.getName());
        msgManager.sendMessageAll(town.getAllMembers(), "ann_mayor_changed", offlinePlayer.getName());
    }


    public void addMember(CommandSender sender, String player, String townName) {
        ValidationResult validate = validateMain(sender, player, townName, "inntown.add", "inntown.admin.add");
        boolean curT = townName == null;
        Town town = validate.town;

        if(!validate.success) {
            return;
        }
        if(curT && (!town.isMayor(sender.getName()) && !town.isDeputy(sender.getName()))) {
            msgManager.sendMessage(sender, "error_add_not_mayordeputy");
            return;
        }
        if(curT && player.equalsIgnoreCase(sender.getName())) {
            msgManager.sendMessage(sender, "error_same_person");
            return;
        }
        player = validate.offlinePlayer.getName();
        Player playerObj = Bukkit.getPlayer(player);
        if(playerObj == null) {
            msgManager.sendMessage(sender, "error_player_offline", player);
            return;
        }
        Town playerTown = dataManager.getPlayerTown(player);
        if(playerTown != null) {
            if(town == playerTown) {
                msgManager.sendMessage(sender, "error_is_member_town", player);
            } else {
                msgManager.sendMessage(sender, "error_is_member", player);
            }
            return;
        }
        Rank rank = townsManager.getConfig().getRank(town.getRank());
        if(rank == null) {
            msgManager.sendMessage(sender, "error_unknown");
            return;
        }
        if(rank.getMaxPlayers() > 0 && town.getAllMembers().size() == rank.getMaxPlayers()) {
            //System.out.printf("town id: %d, rankId: %d, town members: %d rank members: %d", town.getId(), rank.getId(), town.getAllMembers().size(), rank.getMaxPlayers());
            msgManager.sendMessage(sender, "error_max_players");
            return;
        }
        invitations.put(player.toLowerCase(), new InviteData(town, sender.getName()));
        msgManager.sendMessage(sender, "member_added", player);
        msgManager.sendMessage(playerObj, "player_invited", sender.getName(), town.getNameLong());
        msgManager.sendMessage(playerObj, "player_invite_info");
    }

    public void memberAccept(CommandSender sender) {
        if(!sender.hasPermission("inntown.accept")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        String playerName = sender.getName();
        InviteData data = invitations.get(playerName.toLowerCase());
        if(data == null || !data.isActive()) {
            msgManager.sendMessage(sender, "error_aceept_noinvite");
            return;
        }

        invitations.remove(playerName.toLowerCase());
        Town town = data.getTown();
        Rank rank = townsManager.getConfig().getRank(town.getRank());
        if(rank == null) {
            msgManager.sendMessage(sender, "error_unknown");
            return;
        }
        if(rank.getMaxPlayers() > 0 && town.getAllMembers().size() == rank.getMaxPlayers()) {
            msgManager.sendMessage(sender, "error_accept_maxmembers");
            return;
        }
        dataManager.addMember(town, playerName);

        msgManager.sendMessageAll(town.getAllMembers(), "ann_member_added", sender.getName());
        msgManager.sendMessage(data.getInviter(), "member_added_accept", sender.getName());
        msgManager.sendMessage(sender, "player_invite_accept", data.getInviter(), town.getNameLong());
    }

    public void memberDeny(CommandSender sender) {
        if(!sender.hasPermission("inntown.deny")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        String playerName = sender.getName();
        InviteData data = invitations.get(playerName.toLowerCase());
        if(data == null || !data.isActive()) {
            msgManager.sendMessage(sender, "error_aceept_noinvite");
            return;
        }

        invitations.remove(playerName.toLowerCase());
        msgManager.sendMessage(data.getInviter(), "member_added_deny", sender.getName());
        msgManager.sendMessage(sender, "player_invite_deny", data.getInviter(), data.getTown().getNameLong());
    }

    public void removeMember(CommandSender sender, String player, String townName) {
        ValidationResult validate = validateMain(sender, player, townName, "inntown.remove", "inntown.admin.remove");
        boolean curT = townName == null;
        Town town = validate.town;

        if(!validate.success) {
            return;
        }
        if(curT && (!town.isMayor(sender.getName()) && !town.isDeputy(sender.getName()))) {
            msgManager.sendMessage(sender, "error_add_not_mayordeputy");
            return;
        }
        if(curT && player.equalsIgnoreCase(sender.getName())) {
            msgManager.sendMessage(sender, "error_same_person");
            return;
        }
        player = validate.offlinePlayer.getName();
        if(!town.isMember(player)) {
            msgManager.sendMessage(sender, "error_not_member", player);
            return;
        }
        dataManager.removeMember(town, player);
        msgManager.sendMessage(sender, "member_removed", player);
        msgManager.sendMessageAll(town.getAllMembers(), "ann_member_removed", player);
        msgManager.sendMessage(player, "player_removed", town.getNameLong());
    }

    public void createTown(CommandSender sender, String name, String nameLong, String mayor) {
        if(!sender.hasPermission("inntown.admin.create")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        final Town town = dataManager.getTownByName(name);
        if(town != null) {
            msgManager.sendMessage(sender, "error_create_town_exists");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUIDUtils.getPlayerId(mayor));
        if(!offlinePlayer.hasPlayedBefore()) {
            msgManager.sendMessage(sender, "error_unknown_player");
            return;
        }
        mayor = offlinePlayer.getName();
        if(dataManager.getPlayerTown(mayor) != null) {
            msgManager.sendMessage(sender, "error_is_member", mayor);
            return;
        }

        final String senderName = sender.getName();
        final Town createTown = new Town(name, nameLong, mayor, 1);
        new BukkitRunnable(){
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(senderName);
                Integer id = dataManager.createTown(createTown);
                if(id < 0) {
                    msgManager.sendMessage(player, "error_create_unknown");
                    return;
                }
                msgManager.sendMessage(player, "error_create_unknown");
                msgManager.sendMessageAllOnline("ann_town_created", createTown.getNameLong(), createTown.getMayor());
                msgManager.sendMessage(player, "admin_town_created", createTown.getNameLong(),
                        String.valueOf(createTown.getId()), createTown.getMayor());
            }
        }.runTaskAsynchronously(InnCore.getInstance());

    }

    public void setName(CommandSender sender, String townName, String name) {
        Town town;
        if(!sender.hasPermission("inncore.admin.setname")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        town = dataManager.getTownByName(townName);
        if(town == null) {
            msgManager.sendMessage(sender, "error_no_town");
            return;
        }
        if(dataManager.getTownByName(name) != null) {
            msgManager.sendMessage(sender, "error_name_exists");
            return;
        }
        dataManager.setName(town, name);
        msgManager.sendMessage(sender, "admin_name_changed", townName, name);
    }

    public void setNameLong(CommandSender sender, String townName, String nameLong) {
        Town town;
        if(!sender.hasPermission("inncore.admin.setnamelong")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        town = dataManager.getTownByName(townName);
        if(town == null) {
            msgManager.sendMessage(sender, "error_no_town");
            return;
        }
        dataManager.setNameLong(town, nameLong);
        msgManager.sendMessage(sender, "admin_namelong_changed", townName, nameLong);
    }

    public void getId(CommandSender sender, String townName) {
        Town town;
        if(!sender.hasPermission("inncore.admin.id")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        town = dataManager.getTownByName(townName);
        if(town == null) {
            msgManager.sendMessage(sender, "error_no_town");
            return;
        }
        msgManager.sendMessage(sender, "admin_id", town.getName(), String.valueOf(town.getId()));
    }

    public void deleteTown(CommandSender sender, String townName) {
        Town town;
        if(!sender.hasPermission("inncore.admin.id")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        town = dataManager.getTownByName(townName);
        if(town == null) {
            msgManager.sendMessage(sender, "error_no_town");
            return;
        }
        msgManager.sendMessage(sender, "admin_town_deleted", town.getName());
        msgManager.sendMessageAllOnline("ann_town_deleted", town.getName());
        dataManager.deleteTown(town);
    }

    public void setDesc(CommandSender sender, String townName, String desc) {
        ValidationResult validate = validateMain(sender, null, townName, "inntown.desc", "inntown.admin.desc");
        boolean curT = townName == null;
        Town town = validate.town;

        if(!validate.success) {
            return;
        }

        if(curT && !town.isMayor(sender.getName())) {
            msgManager.sendMessage(sender, "error_desc_not_mayor");
            return;
        }

        dataManager.setDescription(town, desc);
        if(curT) {
            msgManager.sendMessage(sender, "desc_changed", desc);
        }
        else {
            msgManager.sendMessage(sender, "admin_desc_changed", town.getNameLong(), desc);
        }
    }

    public void leave(CommandSender sender) {
        ValidationResult validate = validateMain(sender, null, null, "inntown.leave", "inntown.admin,leave");
        if(!validate.success) {
            return;
        }
        Town town = validate.town;
        String player = sender.getName();

        if(town.isMayor(player)) {
            msgManager.sendMessage(sender, "error_leave_is_mayor");
            return;
        }
        dataManager.removeDeputy(town, player);
        dataManager.removeMember(town, player);

        msgManager.sendMessage(sender, "player_left", town.getNameLong());
        msgManager.sendMessageAll(town.getAllMembers(), "ann_player_left", player);
    }

    public void list(CommandSender sender, int page) {
        if(!sender.hasPermission("inntown.list")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }

        List<Town> towns = dataManager.getAllTowns();
        if(towns.isEmpty()) {
            msgManager.sendMessage(sender, "list_notowns");
            return;
        }
        int pages = towns.size() % 6 ==0 ? towns.size() / 6 : towns.size() / 6 + 1;
        page = Math.min(page, pages);
        page--;

        msgManager.sendMessage(sender, "list_head", String.valueOf(page + 1));
        for(int i = page * 6; i < page * 6 + 6 && i < towns.size(); i++) {
            Town town = towns.get(i);
            Rank rank = townsManager.getConfig().getRank(town.getRank());
            msgManager.sendMessage(sender, "list_entry", town.getNameLong(), town.getName(), rank.getName(),
                    String.valueOf(town.getAllMembers().size()), town.getMayor());
        }
        msgManager.sendMessage(sender, "townlot_footer_page", String.valueOf(page + 1), String.valueOf(pages), "town list");
    }

    public void playerInfo(CommandSender sender, String playerName) {

        if(!sender.hasPermission("inntown.player")) {
            msgManager.sendMessage(sender, "error_noperms");
            return;
        }
        if (!Bukkit.getServer().getOfflinePlayer(UUIDUtils.getPlayerId(playerName)).hasPlayedBefore()) {
            msgManager.sendMessage(sender, "error_unknown_player");
            return;
        }

        Town town = dataManager.getPlayerTown(playerName);
        if(town == null) {
            msgManager.sendMessage(sender, "player_info_no", playerName);
            return;
        }
        msgManager.sendMessage(sender, "player_info", playerName, town.getNameLong(), town.getName());
    }

    public void rankup(CommandSender sender, String townName) {
        ValidationResult validate = validateMain(sender, null, townName, "inntown.admin.rankup", "inntown.admin.rankup");
        if(!validate.success) {
            return;
        }
        Town town = validate.town;
        Rank nextRank = townsManager.getConfig().getNextRank(town.getRank());
        if(!nextRank.isReal()) {
            msgManager.sendMessage(sender, "error_no_next_rank");
            return;
        }
        msgManager.sendMessage(sender, "admin_rank", town.getNameLong(), String.valueOf(nextRank.getId()));
        msgManager.sendMessageAllOnline("ann_town_rankup", town.getNameLong(), nextRank.getName(),
                String.valueOf(nextRank.getId()));
        dataManager.setRank(town, nextRank.getId());
    }

    public void setRank(CommandSender sender, String townName, int rankId) {
        ValidationResult validate = validateMain(sender, null, townName, "inntown.admin.setrank", "inntown.admin.setrank");
        if(!validate.success) {
            return;
        }
        Town town = validate.town;
        Rank newRank = townsManager.getConfig().getRank(rankId);
        if(!newRank.isReal()) {
            msgManager.sendMessage(sender, "error_no_next_rank");
            return;
        }
        msgManager.sendMessage(sender, "admin_rank", town.getNameLong(), String.valueOf(newRank.getId()));
        msgManager.sendMessageAllOnline("ann_town_rankup", town.getNameLong(), newRank.getName(),
                String.valueOf(newRank.getId()));
        dataManager.setRank(town, newRank.getId());
    }
}
