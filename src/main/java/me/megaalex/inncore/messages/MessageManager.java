package me.megaalex.inncore.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {


    public static void sendMsg(CommandSender sender, Message type, String... args) {
        String msg = type.getText();

        int argNum = 1;
        for(String arg: args) {
            msg = msg.replaceAll("\\{" + argNum++ + "\\}", arg);
        }
        if(sender == null)
            return;
        sender.sendMessage(formatMessage(msg));
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
