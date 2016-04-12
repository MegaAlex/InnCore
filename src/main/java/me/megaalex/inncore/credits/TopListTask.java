/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.credits;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;
import me.megaalex.inncore.utils.NumberUtils;

public class TopListTask extends BukkitRunnable {

    private final UUID requesterId;

    public TopListTask(UUID requesterId) {
        this.requesterId = requesterId;
    }

    @Override
    public void run() {
        List<CreditsData> dataList = CreditsManager.getTopAccounts(5);
        Player player = Bukkit.getPlayer(requesterId);
        if(player == null || !player.isOnline()) {
             return;
        }
        MessageUtils.sendMsg(player, Message.CREDITS_TOP_HEADER);
        if(dataList == null || dataList.isEmpty()) {
            MessageUtils.sendMsg(player, Message.CREDITS_TOP_NOINFO);
            return;
        }

        for(int i = 1; i < dataList.size(); i++) {
            CreditsData data = dataList.get(i - 1);
            MessageUtils.sendMsg(player, Message.CREDITS_TOP_ENTRY, String.valueOf(i), data.getPlayer(),
                    NumberUtils.parseDecimal(data.getCredits()));
        }

    }
}
