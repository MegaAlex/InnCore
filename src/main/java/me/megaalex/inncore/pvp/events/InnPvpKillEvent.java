package me.megaalex.inncore.pvp.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InnPvpKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player killer;
    private final Player victim;
    private final Location location;
    private final Material item;

    public InnPvpKillEvent(Player killer, Player victim,
                           Location location, Material item) {
        this.killer = killer;
        this.victim = victim;
        this.location = location;
        this.item = item;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
    }

    public Location getLocation() {
        return location;
    }

    public Material getItem() {
        return item;
    }
}
