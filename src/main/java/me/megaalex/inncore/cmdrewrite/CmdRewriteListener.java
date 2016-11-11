package me.megaalex.inncore.cmdrewrite;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import me.megaalex.inncore.InnCore;

public class CmdRewriteListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        if(InnCore.getInstance().getCmdRewriteManager().processCommand(e.getPlayer(), e.getMessage())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsoleCmd(ServerCommandEvent e) {
        if(InnCore.getInstance().getCmdRewriteManager().processCommand(e.getSender(), e.getCommand())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent e) {
        InnCore.getInstance().getCmdRewriteManager().processTabCompete(e.getChatMessage(), e.getTabCompletions());
    }
}
