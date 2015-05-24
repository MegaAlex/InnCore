package me.megaalex.inncore.credits;

import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;
import me.megaalex.inncore.utils.NumberUtils;
import me.megaalex.inncore.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckTask extends BukkitRunnable {

    private UUID senderId;
    private String senderName;
    private String checkName;

    public CheckTask(UUID senderId, String senderName, String checkName) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.checkName = checkName;
    }

    @Override
    public void run() {
        BigDecimal credits = CreditsManager.get(checkName);
        Player player = PlayerUtils.getPlayer(senderId);
        if(player == null)
            return;

        if(senderName.equalsIgnoreCase(checkName)) {
            MessageUtils.sendMsg(player, Message.MORE_HELP);
            MessageUtils.sendMsg(player, Message.YOU_HAVE,
                    NumberUtils.parseDecimal(credits));
        } else {
            MessageUtils.sendMsg(player,Message.USER_HAS,
                    checkName, NumberUtils.parseDecimal(credits));
        }
    }
}
