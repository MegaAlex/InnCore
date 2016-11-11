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

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.ChatCompleteEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Herochat;
import net.md_5.bungee.api.ChatColor;

public class TeamChatListener implements Listener {

    private final SkyBlockManager manager;


    public TeamChatListener(SkyBlockManager manager) {
        this.manager = manager;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChannelChat(ChannelChatEvent e) {
        Player player = e.getSender().getPlayer();
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
            e.setResult(Chatter.Result.FAIL);
            return;
        }
        String format = e.getChannel().applyFormat(e.getFormat(), e.getBukkitFormat(), player);
        String msg = String.format(format, player.getDisplayName(), e.getMessage());
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

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(e.getSender(), e.getChannel(), msg));
        Herochat.logChat(msg);
        e.setResult(Chatter.Result.FAIL);
    }
}
