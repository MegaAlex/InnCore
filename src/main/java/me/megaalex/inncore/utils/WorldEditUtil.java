package me.megaalex.inncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class WorldEditUtil {

    public static Selection getSelection(Player player) {
        WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        return we.getSelection(player);
    }

}
