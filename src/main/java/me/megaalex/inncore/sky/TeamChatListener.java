/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.sky;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.Herochat;
import com.dthielke.MessageHandler;
import com.dthielke.api.ChatResult;
import com.dthielke.api.event.ChannelChatEvent;
import com.dthielke.api.event.ChatCompleteEvent;
import net.md_5.bungee.api.ChatColor;

public class TeamChatListener implements Listener {

    private final SkyBlockManager manager;


    public TeamChatListener(SkyBlockManager manager) {
        this.manager = manager;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChannelChat(ChannelChatEvent e) {
        Player player = e.getChatter().getPlayer();
        int islandLvl = manager.getPlayerIslandLevel(player.getUniqueId());
        e.setFormat(e.getFormat().replace("{ISLAND_LEVEL}", String.valueOf(islandLvl)));

        if(!e.getChannel().getName().equalsIgnoreCase("Team")) {
            return;
        }

        List<UUID> recipients = manager.getTeamMembers(player.getUniqueId());
        if(recipients == null || recipients.isEmpty()) {
            if(player.isOnline()) {
                player.sendMessage(ChatColor.RED + "You are not in a team!");
            }
            e.setResult(ChatResult.FAIL);
            return;
        }
        String msg = MessageHandler.getInstance().formatMessage(e.getChannel(), player, e.getFormat(), e.getMessage());
        //String format = e.getChannel().applyFormat(e.getFormat(), e.getBukkitFormat(), player);
        //String msg = String.format(format, player.getDisplayName(), e.getMessage());
        for (UUID recipientId : recipients) {
            Player recipient = Bukkit.getPlayer(recipientId);
            if(recipient == null || !recipient.isOnline()) {
                continue;
            }
            recipient.sendMessage(msg);
        }

        for(Player spyPlayer : Bukkit.getOnlinePlayers()) {
            if(spyPlayer.hasPermission("inncore.sky.spyteam") && !recipients.contains(spyPlayer.getUniqueId())) {
                spyPlayer.sendMessage(msg);
            }
        }

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(e.getChatter(), e.getChannel(),
                msg, e.getCost(), e.isAsynchronous()));
        Herochat.logChat(msg);
        e.setResult(ChatResult.FAIL);
    }
}
