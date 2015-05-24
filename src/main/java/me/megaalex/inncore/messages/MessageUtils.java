package me.megaalex.inncore.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    public static void sendMsg(CommandSender sender, Message type, String... args) {
        String message = getMessage(type, args);
        sendFormattedMessage(sender, message);
    }

    public static void sendMsgPrefix(CommandSender sender, String prefix, Message type, String... args) {
        String message = prefix + ChatColor.RESET + getMessage(type, args);
        sendFormattedMessage(sender, message);
    }

    public static void sendFormattedMessage(CommandSender sender, String message) {
        if(sender == null)
            return;
        sender.sendMessage(message);
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String formatArgs(String msg, final String[] args) {
        int argNum = 1;
        for(String arg: args) {
            msg = msg.replaceAll("\\{" + argNum++ + "\\}", arg);
        }
        return msg;
    }

    public static String getMessage(Message type, String[] args) {
        String msg = type.getText();
        msg = formatArgs(msg, args);
        return formatMessage(msg);
    }
}
