package me.megaalex.inncore.cmdrewrite;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.megaalex.inncore.InnCore;

public class CmdRewriteListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        InnCore.getInstance().getCmdRewriteManager().processCommand(e.getPlayer(), e.getMessage());
    }
}
